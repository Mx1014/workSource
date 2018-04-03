//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>activityId: 活动id</li>
 * <li>status: 状态，0待确认、1已确认、2已驳回、不填则为全部</li>
 * <li>cancelStatus: 状态，0: cancel, 1: reject, 2:normal 参考{@link com.everhomes.rest.activity.ActivityRosterStatus}</li>
 * <li>pageSize: 每页大小</li>
 * <li>pageOffset: 页码</li>
 * </ul>
 */
public class ListSignupInfoCommand {
	private Long activityId;
	private Integer status;
	private Integer cancelStatus;
	private Integer pageSize;
	private Integer pageOffset;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(Integer cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
