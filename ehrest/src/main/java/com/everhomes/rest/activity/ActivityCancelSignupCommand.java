// @formatter:off
package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 		<li>activityId: 活动Id</li>
 * 		<li>userId: 用户Id，该字段当前仅用于定时取消订单时无法从UserContext.current中获取用户</li>
 * 		<li>cancelType: 自动取消标志  参考 {@link com.everhomes.rest.activity.ActivityCancelType}</li>
 * </ul> 
 *
 */
public class ActivityCancelSignupCommand {
    @NotNull
    private Long activityId;
    
    private Long userId;
    
    private Byte cancelType;
    
    public ActivityCancelSignupCommand() {
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getCancelType() {
		return cancelType;
	}

	public void setCancelType(Byte cancelType) {
		this.cancelType = cancelType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
