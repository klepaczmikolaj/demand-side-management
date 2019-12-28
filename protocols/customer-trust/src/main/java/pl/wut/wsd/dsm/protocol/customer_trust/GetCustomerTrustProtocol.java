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
/**
 * Customer trust protocool. Consists of two simple steps, request and response.
 * No refresh is necessary in protocol because customerTrustService should perform refreshes as often and possible and make sure that
 * response sent is valid.
 * */
public class GetCustomerTrustProtocol extends Protocol {

    private static final ServiceDescriptionFactory descriptionFactory = new ServiceDescriptionFactory();
    @Getter
    private static final GetCustomerTrustProtocol instance = new GetCustomerTrustProtocol();

    /**
     * Request
     */
    public static CustomerTrustRequest customerTrustRequest = new CustomerTrustRequest();
    /**
     * Response containing customer trust ranking.
     */
    public static CustomerTrustResponse customerTrustRankingResponse = new CustomerTrustResponse();


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
