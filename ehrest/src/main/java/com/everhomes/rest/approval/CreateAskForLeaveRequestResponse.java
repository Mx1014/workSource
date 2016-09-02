// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>askForLeaveRequest: 请假信息，参考{@link com.everhomes.rest.approval.AskForLeaveRequestDTO}</li>
 * </ul>
 */
public class CreateAskForLeaveRequestResponse {
	private AskForLeaveRequestDTO askForLeaveRequest;

	public AskForLeaveRequestDTO getAskForLeaveRequest() {
		return askForLeaveRequest;
	}

	public void setAskForLeaveRequest(AskForLeaveRequestDTO askForLeaveRequest) {
		this.askForLeaveRequest = askForLeaveRequest;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
