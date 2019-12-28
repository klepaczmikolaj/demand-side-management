package pl.wut.wsd.dsm.infrastructure.persistence.sort;

class SimplePagination<X> implements Pagination<X> {

    private final int page;
    private final int pageSize;
    private final Sort<X> sort;

    SimplePagination(final int page, final int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        sort = Sort.unsorted();
    }

    SimplePagination(final int page, final int pageSize, final Sort<X> sort) {
        this.page = page;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    @Override
    public int page() {
        return page;
    }

    @Override
    public int pageSize() {
        return pageSize;
    }

    @Override
    public Sort<X> sort() {
        return sort;
    }
}
