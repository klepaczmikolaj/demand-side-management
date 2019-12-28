package pl.wut.wsd.dsm.infrastructure.persistence.sort;

public interface Pagination<X> {
    int page();

    int pageSize();

    default int startPosition() {
        return page() * pageSize();
    }

    Sort<X> sort();

    static <X> Pagination<X> of(final int page, final int size) {
        return new SimplePagination<>(page, size);
    }

    static <X> Pagination<X> sorted(final int page, final int size, final Sort<X> sort) {
        return new SimplePagination<>(page, size, sort);
    }

    static <X> Pagination<X> resultsBetween(final int startIndex, final int endIndex, final Sort<X> sort) {
        return new Pagination<X>() {
            @Override
            public int page() {
                return 0;
            }

            @Override
            public int pageSize() {
                return endIndex - startIndex;
            }

            @Override
            public Sort<X> sort() {
                return sort;
            }
        };
    }
}
