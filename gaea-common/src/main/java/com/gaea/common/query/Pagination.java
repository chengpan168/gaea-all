package com.gaea.common.query;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Pagination<T> {

    /**
     * 当前页，从1开始
     */
    private int                     currentPage = 1;

    /**
     * 每页大小
     */
    private int                     pageSize    = 10;

    /**
     * results
     */
    private List<T>                 data;

    private String                  result;

    private long                    totalCount;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final static Pagination EMPTY       = new Pagination(0, 0, Collections.emptyList());

    @SuppressWarnings("unchecked")
    public static <T> Pagination<T> emptyPaginator() {
        return EMPTY;
    }

    public final static long TotalCountNotSupported = -1L;
    public final static int  TotalPageNotSupported  = -1;

    public Pagination(int pageSize, int currentPage, List<T> data) {
        this.setPageSize(pageSize);
        this.setCurrentPage(currentPage);
        this.setData(data);
        this.setTotalCount(TotalCountNotSupported);
    }

    public Pagination(QueryBase queryBase, Long totalCount) {
        this(queryBase.getPageSize(), queryBase.getCurrentPage(), null, totalCount);
    }

    public Pagination(QueryBase queryBase, List<T> data, Long totalCount) {
        this(queryBase.getPageSize(), queryBase.getCurrentPage(), data, totalCount);
    }

    public Pagination(int pageSize, int currentPage, List<T> data, Long totalCount) {
        this.setPageSize(pageSize);
        this.setCurrentPage(currentPage);
        this.setData(data);
        if (totalCount == null) {
            totalCount = 0L;
        }
        this.setTotalCount(totalCount);

    }

    /**
     * 是否有下一页
     * @return
     */
    public boolean getHasNext() {
        return totalCount / pageSize > currentPage;
    }

    public boolean getIsEmpty() {
        return CollectionUtils.isEmpty(getData());
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 总共多少页
     *
     * @return
     */
    public int getTotalPage() {
        long totalCount = this.getTotalCount();
        if (totalCount != TotalCountNotSupported) {
            int page = Long.valueOf(totalCount / this.getPageSize()).intValue();
            page += totalCount - this.getPageSize() * page > 0 ? 1 : 0;
            return page;
        } else {
            return TotalPageNotSupported;
        }

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
