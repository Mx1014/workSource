package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 属于的上级ID</li>
 * <li>ownerType: 属于上级的类型</li>
 * <li>search: 搜索字符串</li>
 * <li>linkStatus: 连接状态</li>
 * <li>doorType: 门禁类型</li>
 * </ul>
 * @author janson
 *
 */
public class QueryDoorAccessAdminCommand {
    @NotNull
    private Long ownerId;
    
    @NotNull
    private Byte ownerType;
    
    private Long groupId;
    
    private String search;
    private Byte linkStatus;
    private Byte doorType;
    private Long pageAnchor;
    
    private Integer pageSize;
    
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
    public Byte getLinkStatus() {
        return linkStatus;
    }
    public void setLinkStatus(Byte linkStatus) {
        this.linkStatus = linkStatus;
    }
    public Byte getDoorType() {
        return doorType;
    }
    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
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
    
    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
