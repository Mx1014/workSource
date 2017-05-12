// @formatter:off
package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 *<li>activityId:活动ID</li>
 *</ul>
 */
public class CreateSignupOrderCommand {
    @NotNull
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
