//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/11/22.
 */

public class AssetZjhResponse {
    private String responseCode;
    private String errorMsg;
    private Integer totalCount;
    private Integer pageSize;
    private Integer currentPage;
    private String hasNextPag;
    private Integer totalPage;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getHasNextPag() {
        return hasNextPag;
    }

    public void setHasNextPag(String hasNextPag) {
        this.hasNextPag = hasNextPag;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
