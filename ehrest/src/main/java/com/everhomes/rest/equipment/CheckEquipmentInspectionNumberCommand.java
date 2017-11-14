package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerId: 主体id</li>
 * <li>ownerType: 主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * <li>targetId: 所属管理处id</li>
 * <li>targetType: 所属管理处类型</li>
 * <li>checkNumber: 需要校验的编号</li>
 * <li>checkObjectType: 检查对象类型 参考{@link com.everhomes.rest.equipment.CheckObjectType}</li>
 * </ul>
 */
public class CheckEquipmentInspectionNumberCommand {
    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private Long targetId;

    private String targetType;

    private String checkNumber;

    private Byte checkObjectType;

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

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Byte getCheckObjectType() {
        return checkObjectType;
    }

    public void setCheckObjectType(Byte checkObjectType) {
        this.checkObjectType = checkObjectType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
