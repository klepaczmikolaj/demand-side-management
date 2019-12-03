package pl.wut.wsd.dsm.protocol;

import jade.lang.acl.ACLMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DraftProtocol {

    public final Protocol specification = Protocol.builder()
            .name("informQuoteManagerOfClientDecision")
            .step(Steps.sendClientOffer)
            .step(Steps.sendClientDecision)
            .step(Steps.acceptClientDecision)
            .build();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Steps {
        public static ProtocolStep sendClientOffer = ProtocolStep.builder()
                .stepName("Send client offer")
                .performative(ACLMessage.CFP)
                .required(true)
                .messageClass(CustomerOffer.class)
                .targetService("customer-handler")
                .build();

        public static ProtocolStep sendClientDecision = ProtocolStep.builder()
                .stepName("Send client decision")
                .performative(ACLMessage.PROPOSE)
                .required(false)
                .targetService("quote-manager")
                .messageClass(CustomerObligation.class)
                .build();

        public static ProtocolStep acceptClientDecision = ProtocolStep.builder()
                .stepName("Accept client decision")
                .performative(ACLMessage.ACCEPT_PROPOSAL)
                .required(false)
                .targetService("customer-handler")
                .messageClass(CustomerObligation.class)
                .build();
    }
}
