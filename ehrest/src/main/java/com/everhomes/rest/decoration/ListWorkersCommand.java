package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>requestId</li>
 * <li>keyword:关键字</li>
 * <li>pageAnchor</li>
 * <li>pageSize</li>
 * </ul>
 */
public class ListWorkersCommand {

    private Long requestId;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
