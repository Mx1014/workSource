package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/11/21.
 */
public class EbeiJsonEntity<T> {
    private Integer pageSize;

    private Integer totalCount;

    private Integer responseCode;

    private String errorMsg;

    private Integer currentPage;

    private Integer hasNextPag;

    private Integer totalPage;

    private T data;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getHasNextPag() {
        return hasNextPag;
    }

    public void setHasNextPag(Integer hasNextPag) {
        this.hasNextPag = hasNextPag;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
