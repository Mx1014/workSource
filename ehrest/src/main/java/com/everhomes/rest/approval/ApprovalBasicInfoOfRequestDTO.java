// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>approveType: 审批类型，{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>descriptionJson: 描述内容，请假参考{@link com.everhomes.rest.approval.AbsenceBasicDescription}，异常参考{@link com.everhomes.rest.approval.ExceptionRequestBasicDescription}</li>
 * <li>approvalStatus: 审批状态，参考{@link com.everhomes.rest.approval.ApprovalStatus}
 * </li>
 * </ul>
 */
public class ApprovalBasicInfoOfRequestDTO {
	private Byte approveType;
	private String descriptionJson;
	private Byte approvalStatus;

	public ApprovalBasicInfoOfRequestDTO(Byte approveType, Byte approvalStatus) {
		super();
		this.approveType = approveType;
		this.approvalStatus = approvalStatus;
	}

	public Byte getApproveType() {
		return approveType;
	}

	public void setApproveType(Byte approveType) {
		this.approveType = approveType;
	}

	public String getDescriptionJson() {
		return descriptionJson;
	}

	public void setDescriptionJson(String descriptionJson) {
		this.descriptionJson = descriptionJson;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
