package pl.wut.wsd.dsm.protocol.customer_trust;

import jade.lang.acl.ACLMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;
import pl.wut.wsd.dsm.protocol.Protocol;
import pl.wut.wsd.dsm.protocol.ProtocolStep;
import pl.wut.wsd.dsm.protocol.TargetedStep;
import pl.wut.wsd.dsm.service.ServiceDescriptionFactory;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetCustomerTrustProtocol extends Protocol {

    private static final ServiceDescriptionFactory descriptionFactory = new ServiceDescriptionFactory();

    @Getter
    private static final GetCustomerTrustProtocol instance = new GetCustomerTrustProtocol();

    public static CustomerTrustResponse customerTrustRankingResponse = new CustomerTrustResponse();
    public static CustomerTrustRequest customerTrustRequest = new CustomerTrustRequest();


    public static class CustomerTrustRequest extends TargetedStep<GetCustomerTrustProtocol, GetTrustRankingRequest> {

        private CustomerTrustRequest() {
            super("Send customer trust request", ACLMessage.QUERY_REF, true, GetTrustRankingRequest.class, instance, descriptionFactory.nameAndProperties("customer-trust-agent"));
        }

        ACLMessage toRequest(final UUID conversationId, final String content) {
            final ACLMessage message = super.templatedMessage();
            message.setConversationId(conversationId.toString());
            message.setContent(content);

            return message;
        }

    }


    public static class CustomerTrustResponse extends ProtocolStep<GetCustomerTrustProtocol, CustomerTrustRanking> {

        private CustomerTrustResponse() {
            super("Customer trust ranking response", ACLMessage.INFORM, true, CustomerTrustRanking.class, instance);
        }

        public ACLMessage prepareResponse(final String requestConverastionId, final String content) {
            final ACLMessage aclMessage = super.templatedMessage();
            aclMessage.setConversationId(requestConverastionId);
            aclMessage.setContent(content);

            return aclMessage;
        }
    }


}
