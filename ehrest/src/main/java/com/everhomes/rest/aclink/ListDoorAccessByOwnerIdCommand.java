package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 门禁属于的ID</li>
 * <li>ownerType: 门禁从属类型 0 企业， 1 小区 {@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * </ul>
 * @author janson
 *
 */
public class ListDoorAccessByOwnerIdCommand {
    @NotNull
    private Long ownerId;
    
    @NotNull
    private Byte ownerType;
    
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
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
