package com.everhomes.point;

import com.everhomes.util.StringHelper;

import java.util.List;

public class PointCommodityList {

    private List<PointCommodity> rows;

    private Integer pageSize;
    private Integer total;
    private Integer page;
    private Integer pageNo;
    private Boolean hasPrevious;
    private Boolean hasNext;

    public List<PointCommodity> getRows() {
        return rows;
    }

    public void setRows(List<PointCommodity> rows) {
        this.rows = rows;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
