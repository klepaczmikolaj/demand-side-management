package pl.wut.wsd.dsm.agent.customerHandler;


import jade.core.Agent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.customerHandler.handler.ConsumptionMonitor;
import pl.wut.wsd.dsm.agent.customerHandler.handler.CustomerOfferHandler;
import pl.wut.wsd.dsm.agent.customerHandler.handler.OfferAcceptanceHandler;
import pl.wut.wsd.dsm.agent.customerHandler.mapper.CustomerHandlerTypesMapper;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerObligationRepository;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerOfferRepository;
import pl.wut.wsd.dsm.agent.customerHandler.persistence.CustomerRepository;
import pl.wut.wsd.dsm.infrastructure.codec.Codec;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceDiscovery;
import pl.wut.wsd.dsm.infrastructure.discovery.ServiceRegistration;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageHandler;
import pl.wut.wsd.dsm.infrastructure.messaging.MessageSpecification;
import pl.wut.wsd.dsm.infrastructure.messaging.handle.AgentMessagingCapability;
import pl.wut.wsd.dsm.protocol.CustomerDraftProtocol;
import pl.wut.wsd.dsm.protocol.consumption.EnergyConsumptionProtocol;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CustomerHandlerAgent extends Agent {

    @Getter
    private Customer customer;
    private CustomerDraftProtocol customerDraftProtocol = new CustomerDraftProtocol();
    private EnergyConsumptionProtocol.ConsumptionInformation consumptionInformation;

    @Override
    protected void setup() {
        final CustomerHandlerDependencies dependencies = (CustomerHandlerDependencies) getArguments()[0];
        this.customer = dependencies.getCustomer();
        final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(this);
        final CustomerObligationRepository obligationRepo = dependencies.getCustomerObligationRepository();
        final CustomerOfferRepository offerRepo = dependencies.getCustomerOfferRepository();
        final Codec codec = dependencies.getCodec();
        final CustomerHandlerTypesMapper mapper = new CustomerHandlerTypesMapper();
        final AgentMessagingCapability messaging = AgentMessagingCapability.defaultCapability(serviceDiscovery, this);
        consumptionInformation = EnergyConsumptionProtocol.INSTANCE.informationStep(customer.getCustomerId());

        final CustomerRepository customerRepo = dependencies.getCustomerRepository();
        onBoardCustomerIfDoesntExist(customerRepo, customer);

        final CustomerOfferHandler customerOfferHandler = new CustomerOfferHandler(codec, new CustomerDraftProtocol().sendCustomerOffer(), this, serviceDiscovery, offerRepo, mapper, customerRepo);
        final OfferAcceptanceHandler offerAcceptanceHandler = new OfferAcceptanceHandler(codec, customerDraftProtocol.acceptClientDecision(), offerRepo, obligationRepo, messaging, customerDraftProtocol.informOfCustomerHandlerAcceptance(), customerRepo);
        final ConsumptionMonitor consumptionMonitor = new ConsumptionMonitor(codec, consumptionInformation, obligationRepo, customer.getCustomerId());

        this.addBehaviour(
                new MessageHandler(this,
                        MessageSpecification.of(customerDraftProtocol.sendOfferToHandler().toMessageTemplate(), customerOfferHandler::handle),
                        MessageSpecification.of(customerDraftProtocol.acceptClientDecision().toMessageTemplate(), offerAcceptanceHandler::handle),
                        MessageSpecification.of(consumptionInformation.toMessageTemplate(), consumptionMonitor::handle))
        );
        registerToWhitepages();
    }

    private void onBoardCustomerIfDoesntExist(final CustomerRepository customerRepo, final Customer customer) {
        final Optional<pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer> searchResult = customerRepo.findByCustomerId(customer.getCustomerId());
        if (!searchResult.isPresent()) {
            customerRepo.save(pl.wut.wsd.dsm.agent.customerHandler.domain.model.Customer.builder()
                    .customerId(customer.getCustomerId())
                    .login(UUID.randomUUID().toString().substring(0, 8))
                    .name(UUID.randomUUID().toString().substring(0, 9))
                    .nominalUsageInWatts(4000)
                    .build());
        }
    }

    private void registerToWhitepages() {
        final ServiceRegistration serviceRegistration = new ServiceRegistration(this);

        serviceRegistration.registerRetryOnFailure(Duration.ofSeconds(1),
                customerDraftProtocol.sendOfferToHandler().serviceDescription(customer),
                customerDraftProtocol.acceptClientDecision().serviceDescription(customer));
    }


}
