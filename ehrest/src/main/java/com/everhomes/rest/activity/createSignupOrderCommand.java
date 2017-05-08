// @formatter:off
package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 *<li>activityRosterId:活动报名ID</li>
 *</ul>
 */
public class createSignupOrderCommand {
    @NotNull
    private Long activityRosterId;
   
	public Long getActivityRosterId() {
		return activityRosterId;
	}

	public void setActivityRosterId(Long activityRosterId) {
		this.activityRosterId = activityRosterId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
