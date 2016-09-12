package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkQueryLogCommand {
    private Byte ownerType;
    private Long ownerId;
    private Long eventType;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;
    
    public Long getEventType() {
        return eventType;
    }
    public void setEventType(Long eventType) {
        this.eventType = eventType;
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

    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
