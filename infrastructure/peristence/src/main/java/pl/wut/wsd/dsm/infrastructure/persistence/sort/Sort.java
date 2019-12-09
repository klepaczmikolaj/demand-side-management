package pl.wut.wsd.dsm.infrastructure.persistence.sort;

import javax.persistence.metamodel.SingularAttribute;

public interface Sort<X> {
    SingularAttribute<X, ?> sortedBy();

    SortType type();

    static <X> Sort<X> unsorted() {
        return new Sort<X>() {
            @Override
            public SingularAttribute<X, ?> sortedBy() {
                return null;
            }

            @Override
            public SortType type() {
                return SortType.UNSORTED;
            }
        };
    }

    static <X> Sort<X> ascending(final SingularAttribute<X, ?> attribute) {
        return new Sort<X>() {
            @Override
            public SingularAttribute<X, ?> sortedBy() {
                return attribute;
            }

            @Override
            public SortType type() {
                return SortType.ASCENDING;
            }
        };
    }

    static <X> Sort<X> descending(final SingularAttribute<X, ?> attribute) {
        return new Sort<X>() {
            @Override
            public SingularAttribute<X, ?> sortedBy() {
                return attribute;
            }

            @Override
            public SortType type() {
                return SortType.DESCENDING;
            }
        };
    }
}
