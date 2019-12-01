package pl.wut.wsd.dsm.agent.customer_handler.infrastructure.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface Specification<T> {
    Predicate toPredicate(final Root<T> root, final CriteriaBuilder cb);
}
