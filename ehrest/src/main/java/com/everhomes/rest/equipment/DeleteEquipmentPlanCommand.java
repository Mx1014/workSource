package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 巡检计划id</li>
 * <li>ownerId: 计划所属的主体id</li>
 * <li>ownerType: 计划所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * <li>targetId: 项目id</li>
 * </ul>
 */
public class DeleteEquipmentPlanCommand {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private Long targetId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
