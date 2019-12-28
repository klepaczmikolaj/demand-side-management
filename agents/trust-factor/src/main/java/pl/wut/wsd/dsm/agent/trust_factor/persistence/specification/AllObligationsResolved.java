package pl.wut.wsd.dsm.agent.trust_factor.persistence.specification;

import lombok.RequiredArgsConstructor;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.CustomerObligationState;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation;
import pl.wut.wsd.dsm.agent.trust_factor.persistence.model.Obligation_;
import pl.wut.wsd.dsm.infrastructure.persistence.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(staticName = "after")
public class AllObligationsResolved implements Specification<Obligation> {

    private final static List<CustomerObligationState> resolvedStates =
            Arrays.asList(CustomerObligationState.FAILED, CustomerObligationState.KEPT);
    private final ZonedDateTime after;

    @Override
    public Predicate toPredicate(final Root<Obligation> root, final CriteriaBuilder cb) {
        final Predicate modificationAfterGivenDate = cb.greaterThan(root.get(Obligation_.until), after);
        final Predicate resolved = root.get(Obligation_.state).in(resolvedStates);

        return cb.and(modificationAfterGivenDate, resolved);
    }

}
