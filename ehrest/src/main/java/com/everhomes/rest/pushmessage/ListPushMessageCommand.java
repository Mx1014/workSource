package com.everhomes.rest.pushmessage;

public class ListPushMessageCommand {
    private Long pageAnchor;
    private Integer pageSize;
    
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
