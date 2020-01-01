package pl.wut.wsd.dsm.protocol;

import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.experimental.Accessors;
import pl.wut.wsd.dsm.ontology.network.DraftSummary;
import pl.wut.wsd.dsm.ontology.network.ExpectedInbalancement;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

/**
 * Interaction protocol specifying communication between system core and draft management.
 * Flow:
 * Network Advisory -> Quote Management [Expected system inbalancement]
 */
@Accessors(fluent = true)
public class SystemDraftProtocol extends Protocol {

    private final ServiceDescriptionFactory serviceDescriptionFactory = new ServiceDescriptionFactory();

    @Getter
    /**
     * Informs quote manager of expected inbalancement. Quote manager then decides whether to start new draft.
     */
    private TargetedStep<SystemDraftProtocol, ExpectedInbalancement> informQuoteManagerOfExpectedInbalancement =
            TargetedStep.<SystemDraftProtocol, ExpectedInbalancement>targetedBuilder()
                    .stepName("Inform quote manager of expected inbalancement")
                    .performative(ACLMessage.INFORM)
                    .required(true)
                    .targetService(serviceDescriptionFactory.name("quote-manager"))
                    .messageClass(ExpectedInbalancement.class)
                    .protocol(this)
                    .build();

    /**
     * After quote manager starts draft it should periodically update network advisory with draft details
     * using this information network advisor can adjust demand profile.
     */
    @Getter
    private TargetedStep<SystemDraftProtocol, DraftSummary> updateWithDraftSummary =
            TargetedStep.<SystemDraftProtocol, DraftSummary>targetedBuilder()
                    .stepName("Update network advisory with current draft status")
                    .performative(ACLMessage.INFORM)
                    .required(true)
                    .targetService(serviceDescriptionFactory.name("network-advisor"))
                    .messageClass(DraftSummary.class)
                    .protocol(this)
                    .build();

}
