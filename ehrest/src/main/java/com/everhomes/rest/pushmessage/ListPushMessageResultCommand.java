package com.everhomes.rest.pushmessage;

public class ListPushMessageResultCommand {
    private Long pageAnchor;
    private Integer pageSize;
    
    private String idenfity;

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

    public String getIdenfity() {
        return idenfity;
    }

    public void setIdenfity(String idenfity) {
        this.idenfity = idenfity;
    }
}
