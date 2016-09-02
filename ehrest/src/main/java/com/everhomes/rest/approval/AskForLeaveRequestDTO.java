// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>requestToken: 申请的token</li>
 * <li>categoryName: 请假类型</li>
 * <li>reason: 请假理由</li>
 * <li>timeTotal: 请假总时长</li>
 * <li>timeRangeList: 时间列表，参考{@link com.everhomes.rest.approval.TimeRange}</li>
 * <li>approvalStatus: 审批状态</li>
 * </ul>
 */
public class AskForLeaveRequestDTO {
	private String requestToken;
	private String categoryName;
	private String reason;
	private String timeTotal;
	private List<TimeRange> timeRangeList;
	private Byte approvalStatus;

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTimeTotal() {
		return timeTotal;
	}

	public void setTimeTotal(String timeTotal) {
		this.timeTotal = timeTotal;
	}

	public List<TimeRange> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<TimeRange> timeRangeList) {
		this.timeRangeList = timeRangeList;
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
