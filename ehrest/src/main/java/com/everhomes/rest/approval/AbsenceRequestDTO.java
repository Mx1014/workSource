// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>requestId: 申请ID</li>
 * <li>nickName: 姓名</li>
 * <li>categoryName: 请假类型名称</li>
 * <li>reason: 申请理由</li>
 * <li>timeRangeList: 请假时间列表</li>
 * </ul>
 */
public class AbsenceRequestDTO {
	private Long requestId;
	private String nickName;
	private String categoryName;
	private String reason;
	private List<TimeRange> timeRangeList;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<TimeRange> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<TimeRange> timeRangeList) {
		this.timeRangeList = timeRangeList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
