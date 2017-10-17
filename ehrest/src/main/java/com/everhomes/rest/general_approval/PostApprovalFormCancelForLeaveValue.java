package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>originalStartTime: 请假开始时间</li>
 * <li>originalEndTime: 请假结束时间</li>
 * <li>originalDuration: 请假时长</li>
 * <li>cancelStartTime: 销假开始时间</li>
 * <li>cancelEndTime: 销假结束时间</li>
 * <li>cancelDuration: 销假时长</li>
 * </ul>
 */
public class PostApprovalFormCancelForLeaveValue {

	private String originalStartTime;

	private String originalEndTime;

	private String originalDuration;

	private String cancelStartTime;

	private String cancelEndTime;

	private String cancelDuration;

	public String getOriginalStartTime() {
		return originalStartTime;
	}

	public void setOriginalStartTime(String originalStartTime) {
		this.originalStartTime = originalStartTime;
	}

	public String getOriginalEndTime() {
		return originalEndTime;
	}

	public void setOriginalEndTime(String originalEndTime) {
		this.originalEndTime = originalEndTime;
	}

	public String getOriginalDuration() {
		return originalDuration;
	}

	public void setOriginalDuration(String originalDuration) {
		this.originalDuration = originalDuration;
	}

	public String getCancelStartTime() {
		return cancelStartTime;
	}

	public void setCancelStartTime(String cancelStartTime) {
		this.cancelStartTime = cancelStartTime;
	}

	public String getCancelEndTime() {
		return cancelEndTime;
	}

	public void setCancelEndTime(String cancelEndTime) {
		this.cancelEndTime = cancelEndTime;
	}

	public String getCancelDuration() {
		return cancelDuration;
	}

	public void setCancelDuration(String cancelDuration) {
		this.cancelDuration = cancelDuration;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
