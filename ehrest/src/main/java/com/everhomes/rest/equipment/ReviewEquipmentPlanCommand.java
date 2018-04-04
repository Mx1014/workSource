package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: planId</li>
 *  <li>reviewResult: 审批结果 参考{@link com.everhomes.rest.equipment.ReviewResult}</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * </ul>
 */
public class ReviewEquipmentPlanCommand {
    private Long id;

    private Long ownerId;

    private String ownerType;

    private Long targetId;

    private Byte reviewResult;

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

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
