// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>ownerId: 所属者ID</li>
 * <li>timeUnit: 请假单位,参考{@link com.everhomes.rest.approval.ApprovalCategoryTimeUnit}</li>
 * <li>timeStep: 最小请假时长，单位对应timeUnit</li>
 * <li>remainderFlag: 是否支持关联余额，1表示支持，0不支持，参考{@link com.everhomes.rest.approval.ApprovalCategoryReminderFlag}</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.approval.ApprovalCategoryStatus}</li>
 * </ul>
 */
public class UpdateApprovalCategoryCommand {
    private Long id;
    private Long ownerId;
    private String timeUnit;
    private Double timeStep;
    private Byte remainderFlag;
    private Byte status;

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

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(Double timeStep) {
        this.timeStep = timeStep;
    }

    public Byte getRemainderFlag() {
        return remainderFlag;
    }

    public void setRemainderFlag(Byte remainderFlag) {
        this.remainderFlag = remainderFlag;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
