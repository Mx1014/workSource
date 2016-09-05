// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>askForLeaveRequest: 请假信息，参考{@link com.everhomes.rest.approval.ApprovalRequestDTO}</li>
 * </ul>
 */
public class CreateAskForLeaveRequestResponse {
	private ApprovalRequestDTO askForLeaveRequest;

	public ApprovalRequestDTO getAskForLeaveRequest() {
		return askForLeaveRequest;
	}

	public void setAskForLeaveRequest(ApprovalRequestDTO askForLeaveRequest) {
		this.askForLeaveRequest = askForLeaveRequest;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
