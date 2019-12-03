package pl.wut.wsd.dsm.protocol;

import jade.domain.FIPAAgentManagement.Property;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.experimental.Accessors;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

import java.util.Objects;

@Accessors(fluent = true)
public class DraftProtocol extends Protocol {

    @Getter
    private final ProtocolStep sendClientOffer;

    @Getter
    private final ProtocolStep sendClientDecision;

    @Getter
    private final ProtocolStep acceptClientDecision;

    public static DraftProtocol forCustomer(final Customer customer) {
        return new DraftProtocol(Objects.requireNonNull(customer));
    }

    private DraftProtocol(final Customer customer) {
        final ServiceDescriptionFactory factory = new ServiceDescriptionFactory();

        sendClientOffer = ProtocolStep.builder()
                .stepName("Send client offer")
                .performative(ACLMessage.CFP)
                .required(true)
                .messageClass(CustomerOffer.class)
                .targetService(factory.nameAndProperties("customer-handler", new Property("customerId", customer.getCustomerId())))
                .protocol(this)
                .build();

        sendClientDecision = ProtocolStep.builder()
                .stepName("Send client decision")
                .performative(ACLMessage.PROPOSE)
                .required(false)
                .targetService(factory.nameAndProperties("quote-manager"))
                .messageClass(CustomerObligation.class)
                .protocol(this)
                .build();

        acceptClientDecision = ProtocolStep.builder()
                .stepName("Accept client decision")
                .performative(ACLMessage.ACCEPT_PROPOSAL)
                .required(false)
                .targetService(factory.nameAndProperties("customer-handler", new Property("customerId", customer.getCustomerId())))
                .messageClass(CustomerObligation.class)
                .protocol(this)
                .build();
    }


}
