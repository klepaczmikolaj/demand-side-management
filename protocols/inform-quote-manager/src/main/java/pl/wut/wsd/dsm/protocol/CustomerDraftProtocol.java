package pl.wut.wsd.dsm.protocol;

import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.ontology.draft.CustomerObligation;
import pl.wut.wsd.dsm.ontology.draft.CustomerOffer;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

import static jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL;
import static jade.lang.acl.ACLMessage.CFP;

@Accessors(fluent = true)
public class CustomerDraftProtocol extends Protocol {

    @Getter
    private final SendClientOffer sendClientOffer;

    @Getter
    /** Send client decision to quote manager. */
    private final TargetedStep sendClientDecision;

    @Getter
    private final AcceptClientDecision acceptClientDecision;


    public CustomerDraftProtocol() {
        final ServiceDescriptionFactory factory = new ServiceDescriptionFactory();

        sendClientOffer = new SendClientOffer(this);

        sendClientDecision = TargetedStep.<CustomerDraftProtocol, CustomerObligation>targetedBuilder()
                .stepName("Send client decision")
                .performative(ACLMessage.PROPOSE)
                .required(false)
                .targetService(factory.nameAndProperties("quote-manager"))
                .messageClass(CustomerObligation.class)
                .protocol(this)
                .build();

        acceptClientDecision = new AcceptClientDecision(this);
    }

    public static class SendClientOffer extends ProtocolStep<CustomerDraftProtocol, CustomerOffer> {
        private final ServiceDescriptionFactory serviceDescriptionFactory = new ServiceDescriptionFactory();

        private SendClientOffer(final @NonNull CustomerDraftProtocol protocol) {
            super("Send client offer", CFP, true, CustomerOffer.class, protocol);
        }

        public ServiceDescription serviceDescription(final Customer targetCustomer) {
            return serviceDescriptionFactory.
                    nameAndProperties("customer-handler", new Property("customerId", targetCustomer.getCustomerId()));
        }
    }

    public static class AcceptClientDecision extends ProtocolStep<CustomerDraftProtocol, CustomerObligation> {
        private final ServiceDescriptionFactory serviceDescriptionFactory = new ServiceDescriptionFactory();

        protected AcceptClientDecision(final @NonNull CustomerDraftProtocol protocol) {
            super("Send client decision", ACCEPT_PROPOSAL, false, CustomerObligation.class, protocol);
        }

        public ServiceDescription serviceDescription(final Customer targetCustomer) {
            return serviceDescriptionFactory
                    .nameAndProperties("customer-handler", new Property("customerId", targetCustomer.getCustomerId()));
        }
    }
}
