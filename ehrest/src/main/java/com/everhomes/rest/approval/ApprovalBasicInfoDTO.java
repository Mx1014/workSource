// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>title: 标题</li>
 * <li>descriptionJson: 描述内容，请假参考{@link com.everhomes.rest.approval.AskForLeaveBasicDescription}，忘打卡参考{@link com.everhomes.rest.approval.ForgetToPunchBasicDescription}</li>
 * <li>approvalStatus: 审批状态，参考{@link com.everhomes.rest.approval.ApprovalStatus}
 * </li>
 * </ul>
 */
public class ApprovalBasicInfoDTO {
	private String title;
	private String descriptionJson;
	private Byte approvalStatus;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
