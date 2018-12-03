// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType：所属组织类型 0小区 1企业 {@link DoorAccessOwnerType}</li>
 * <li>ownerId: 所属组织Id</li>
 * <li>status: 状态 null 保持原状 0关闭 1 开启</li>
 * </ul>
 *
 */
public class VisitorComingNoticeCommand {
    private Byte ownerType;
    private Long ownerId;
    private Byte status;


    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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