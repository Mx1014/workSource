// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType：所属组织类型 0小区 1企业 {@link DoorAccessOwnerType}</li>
 * <li>ownerId: 所属组织Id</li>
 * <li>status: 状态</li>
 * <li>phone: 服务热线</li>
 * </ul>
 *
 */
public class UpdateServiceHotlineCommand {
    private Byte ownerType;
    private Long ownerId;
    private Byte status;
    private String phone;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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