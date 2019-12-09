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

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetCustomerTrustProtocol extends Protocol {

    private static final ServiceDescriptionFactory descriptionFactory = new ServiceDescriptionFactory();

    @Getter
    private static final GetCustomerTrustProtocol instance = new GetCustomerTrustProtocol();

    public static TargetedStep<GetCustomerTrustProtocol, GetTrustRankingRequest> customerTrustRequest = TargetedStep.
            <GetCustomerTrustProtocol, GetTrustRankingRequest>targetedBuilder()
            .stepName("Send customer trust request")
            .messageClass(GetTrustRankingRequest.class)
            .protocol(instance)
            .targetService(descriptionFactory.nameAndProperties("customer-trust-agent"))
            .required(true)
            .performative(ACLMessage.QUERY_REF)
            .build();

    public static ProtocolStep<GetCustomerTrustProtocol, CustomerTrustRanking> customerTrustRankingResponse = ProtocolStep.
            <GetCustomerTrustProtocol, CustomerTrustRanking>builder()
            .stepName("Customer trust ranking response")
            .messageClass(CustomerTrustRanking.class)
            .protocol(instance)
            .required(true)
            .performative(ACLMessage.INFORM)
            .build();

}
