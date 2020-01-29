package pl.wut.wsd.dsm.agent.network_advisor;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.wut.wsd.dsm.agent.network_advisor.domain.ElectrictyDemandCalculatorWithDraftUpdater;
import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityDemandProfile;
import pl.wut.wsd.dsm.agent.network_advisor.domain.profile.ElectricityProductionProfile;
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast;
import pl.wut.wsd.dsm.infrastructure.common.function.Result;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceRegistration;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.ontology.network.DemandAndProduction;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.protocol.SystemDraftProtocol;

import java.math.BigInteger;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class NetworkAgent extends Agent {

    private NetworkAgentDependencies dependencies;
    private final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);
    private final SystemDraftProtocol systemDraftProtocol = new SystemDraftProtocol();
    private WeatherForecast weatherForecast;
    private ElectricityProductionProfile productionProfile;
    private ElectricityDemandProfile demandProfile;

    @Override
    protected void setup() {
        dependencies = (NetworkAgentDependencies) this.getArguments()[0];
        this.weatherForecast = dependencies.weatherAdapter().getWeatherForecast();
        this.productionProfile = dependencies.electricityProductionProfileCalculator().calculate(weatherForecast);
        this.demandProfile = dependencies.electricityDemandProfileCalculator().calculate(weatherForecast);

        /* Refresh weather behaviour */
        addBehaviour(weatherUpdater());
        /* Refresh production profile */
        addBehaviour(new TickerBehaviour(this, dependencies.productionProfileRefreshFrequency().toMillis()) {
            @Override
            protected void onTick() {
                log.info("Refreshing production profile");
                productionProfile = dependencies.electricityProductionProfileCalculator().calculate(weatherForecast);
            }
        });
        /* Refresh demand profile */
        addBehaviour(new TickerBehaviour(this, dependencies.demandProfileRefreshFrequency().toMillis()) {
            @Override
            protected void onTick() {
                log.info("Refreshing demand profile");
                demandProfile = dependencies.electricityDemandProfileCalculator().calculate(weatherForecast);
            }
        });

        /* Inform quote manager of inbalancement */
        addBehaviour(inbalancementChecker());
        final ElectrictyDemandCalculatorWithDraftUpdater updater = new ElectrictyDemandCalculatorWithDraftUpdater(dependencies.codec(),
                systemDraftProtocol.updateWithDraftSummary(),
                dependencies.electricityDemandProfileCalculator());
        addBehaviour(new MessageHandler(this, MessageSpecification.of(
                systemDraftProtocol.updateWithDraftSummary().toMessageTemplate(),
                updater::handle)));
        new ServiceRegistration(this).registerRetryOnFailure(Duration.ofSeconds(5), systemDraftProtocol.updateWithDraftSummary().getTargetService());
    }

    @NotNull
    private TickerBehaviour inbalancementChecker() {
        final Duration checkFrequency = dependencies.inbalancementCheckRefreshFrequency();
        final Duration checkInAdvance = dependencies.inbalancementRefreshAdvancement();

        return new TickerBehaviour(this, checkFrequency.toMillis()) {
            @Override
            protected void onTick() {
                log.info("checking inbalancement");
                final DemandAndProduction demandAndProduction = calculateDemandAndProduction();

                log.info("Demand and production: {}", demandAndProduction);
                if (safetyTresholdNotKept(demandAndProduction)) {
                    final ExpectedInbalancement expectedInbalancement = ExpectedInbalancement.builder()
                            .since(ZonedDateTime.now().plus(checkInAdvance))
                            .until(ZonedDateTime.now().plus(checkFrequency).plus(checkInAdvance))
                            .expectedDemandAndProduction(demandAndProduction)
                            .build();

                    final Result<List<AID>, FIPAException> services = serviceDiscovery.findServices(systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().getTargetService())
                            .mapResult(l -> l.stream().map(DFAgentDescription::getName).collect(Collectors.toList()));

                    if (services.isError()) {
                        log.error("Could not find quote service!", services.error());
                    } else if (services.result().isEmpty()) {
                        log.error("Could not find quote service, service discovery returned no result");
                    } else {
                        final AID quoteManager = services.result().get(0);
                        final ACLMessage message = systemDraftProtocol.informQuoteManagerOfExpectedInbalancement().templatedMessage();
                        message.setLanguage(dependencies.codec().language());
                        message.setContent(dependencies.codec().encode(expectedInbalancement));
                        message.addReceiver(quoteManager);

                        send(message);
                    }
                }
            }

            private DemandAndProduction calculateDemandAndProduction() {
                final long limit = checkFrequency.toMinutes() == 0 ?
                        1 : checkFrequency.toMinutes();

                final double averageDemand = averageBetween(limit, ZonedDateTime.now(), ZonedDateTime.now().plus(checkFrequency), demandProfile::getDemandInWatts);
                final double averageProduction = averageBetween(limit, ZonedDateTime.now(), ZonedDateTime.now().plus(checkFrequency), productionProfile::getProductionInWatts);

                return new DemandAndProduction(averageDemand, averageProduction);
            }

            private boolean safetyTresholdNotKept(final DemandAndProduction demandAndProduction) {
                boolean b = Math.abs(demandAndProduction.getWattsProduction() - demandAndProduction.getWattsDemand()) < dependencies.safetyTresholdWatts();
                log.info("Safety treshold kept? {}", !b);
                return b;
            }

            private double averageBetween(final long steps,
                                          final ZonedDateTime start,
                                          final ZonedDateTime end,
                                          final Function<ZonedDateTime, BigInteger> computation) {
                final ToIntFunction<ZonedDateTime> toIntFunction = z -> computation.apply(z).intValue();
                final Duration duration = Duration.between(start, end);
                final double step = (double) duration.getSeconds() / steps;

                return IntStream.iterate(0, i -> i + (int) step)
                        .limit(steps)
                        .mapToObj(i -> ZonedDateTime.now().plusSeconds(i))
                        .mapToInt(toIntFunction)
                        .average()
                        .orElse(0);
            }
        };
    }

    @NotNull
    private TickerBehaviour weatherUpdater() {
        return new TickerBehaviour(this, dependencies.weatherRefreshDuration().toMillis()) {
            @Override
            protected void onTick() {
                updateWeather();
            }

            private void updateWeather() {
                try {
                    weatherForecast = dependencies.weatherAdapter().getWeatherForecast();
                    log.info("Forecast refreshed");
                } catch (final Exception e) {
                    log.error("Could not update forecast", e);
                }
            }
        };
    }

}

