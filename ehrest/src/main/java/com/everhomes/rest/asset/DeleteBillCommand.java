package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: 账单id</li>
 *     <li>ownerId：账单所属物业公司id</li>
 *     <li>ownerType：账单所属物业公司类型</li>
 *     <li>targetId：账单所属园区id</li>
 *     <li>targetType：账单所属园区类型</li>
 * </ul>
 */
public class DeleteBillCommand {
    private Long id;

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
