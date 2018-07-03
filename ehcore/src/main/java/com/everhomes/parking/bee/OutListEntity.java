// @formatter:off
package com.everhomes.parking.bee;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 15:30
 */
public class OutListEntity<T> {
    private T datalist;
    private String message;
    private String pageIndex;
    private String pageSize;
    private String state;
    private String totalCount;

    public T getDatalist() {
        return datalist;
    }

    public void setDatalist(T datalist) {
        this.datalist = datalist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
