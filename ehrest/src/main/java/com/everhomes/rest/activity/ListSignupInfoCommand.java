//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>activityId: 活动id</li>
 * <li>pageSize: 每页大小</li>
 * <li>pageAnchor: 锚点</li>
 * </ul>
 */
public class ListSignupInfoCommand {
	private Long activityId;
	private Integer pageSize;
	private Long pageAnchor;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
