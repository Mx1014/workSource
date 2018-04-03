// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

/**
 * 活动报名详情的actionData
 * <ul>
 * <li>activityId: 活动id</li>
 * </ul>
 */
public class ActivityEnrollDetailActionData {
	private Long activityId;

	public ActivityEnrollDetailActionData() {
		super();
	}

	public ActivityEnrollDetailActionData(Long activityId) {
		super();
		this.activityId = activityId;
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
	
	public String toUrlString(String url) {
		return url.replace("${activityId}", String.valueOf(activityId));
	}
}
