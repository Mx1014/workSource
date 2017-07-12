package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>doorId: 门禁ID</li>
 * <li>userId: 用户id</li>
 * <li></li>
 * </ul>
 * 
 */
public class ListDoorAuthLogCommand {
    private Long pageAnchor;

    @NotNull
    private Long doorId;
    
    private Integer pageSize;

    private Long userId;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
