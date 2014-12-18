package org.iatoki.judgels.commons;

import java.util.List;

public class Page<T> {

    private final long pageSize;
    private final long totalRowCount;
    private final long pageIndex;
    private List<T> list;

    public Page(List<T> data, long total, long page, long pageSize) {
        this.list = data;
        this.totalRowCount = total;
        this.pageIndex = page;
        this.pageSize = pageSize;
    }

    public long getTotalRowCount() {
        return totalRowCount;
    }

    public long getPageIndex() {
        return pageIndex;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getTotalPageCount() {
        return totalRowCount / pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public boolean hasPrev() {
        return pageIndex > 1;
    }

    public boolean hasNext() {
        return (totalRowCount / pageSize) > pageIndex;
    }

}
