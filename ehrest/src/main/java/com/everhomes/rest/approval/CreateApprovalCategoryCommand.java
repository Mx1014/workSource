// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者ID</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryName: 类别名称</li>
 * <li>timeUnit: 请假单位,参考{@link com.everhomes.rest.approval.ApprovalCategoryTimeUnit}</li>
 * <li>timeStep: 最小请假时长，单位对应timeUnit</li>
 * <li>remainderFlag: 是否支持关联余额，1表示支持，0不支持，参考{@link com.everhomes.rest.approval.ApprovalCategoryReminderFlag}</li>
 * </ul>
 */
public class CreateApprovalCategoryCommand {
	private String ownerType;
	private Long ownerId;
	private Byte approvalType;
	private String categoryName;
	private String timeUnit;
	private Double timeStep;
	private Byte remainderFlag;

	public Byte getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Byte approvalType) {
		this.approvalType = approvalType;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
