package com.everhomes.visitorsys.hkws;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Map;

public class HKWSDataSet<T> {
    private Integer total;
    private Integer pageNo;
    private Integer pageSize;
    private List<T> list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
