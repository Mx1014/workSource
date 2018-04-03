//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>activityId: 活动id</li>
 * </ul>
 */
public class ImportSignupInfoCommand {
	private Long activityId;

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
