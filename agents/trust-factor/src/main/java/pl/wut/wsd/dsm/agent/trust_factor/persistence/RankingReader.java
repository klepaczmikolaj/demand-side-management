package pl.wut.wsd.dsm.agent.trust_factor.persistence;

import lombok.RequiredArgsConstructor;
import pl.wut.dsm.ontology.customer.Customer;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerTrust_;
import pl.wut.wsd.dsm.infrastructure.persistence.hibernate.HibernateTemplate;
import pl.wut.wsd.dsm.infrastructure.persistence.sort.Pagination;
import pl.wut.wsd.dsm.infrastructure.persistence.sort.Sort;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRanking;
import pl.wut.wsd.dsm.ontology.trust.CustomerTrustRankingEntry;
import pl.wut.wsd.dsm.ontology.trust.GetTrustRankingRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RankingReader {
    private final HibernateTemplate hibernateTemplate;

    public CustomerTrustRanking getRanking(final GetTrustRankingRequest request) {
        final int from = request.getFrom();
        final int to = request.getTo();


        final List<CustomerTrust> entries = hibernateTemplate.findAll((r, cb) -> cb.and(), CustomerTrust.class, Pagination.resultsBetween(from, to, Sort.descending(CustomerTrust_.currentValue)));
        final AtomicInteger rank = new AtomicInteger(from);
        final Map<Integer, CustomerTrust> rankAndEntry = entries.stream().collect(Collectors.toMap(c -> rank.getAndIncrement(), Function.identity()));

        final List<CustomerTrustRankingEntry> ranking = rankAndEntry.entrySet()
                .stream()
                .map(this::toEntry)
                .collect(Collectors.toList());

        return CustomerTrustRanking.ofEntries(ranking);
    }

    private CustomerTrustRankingEntry toEntry(final Map.Entry<Integer, CustomerTrust> rankAndTrust) {
        final CustomerTrust trust = rankAndTrust.getValue();

        return CustomerTrustRankingEntry.of(rankAndTrust.getKey(), new Customer(trust.getCustomerId()), trust.getCurrentValue());
    }
}
