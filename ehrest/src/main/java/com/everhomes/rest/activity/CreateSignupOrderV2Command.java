// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>activityId: 活动ID</li>
 *     <li>clientAppName: Realm值</li>
 *     <li>openid: openid，微信公众号需要穿openid</li>
 * </ul>
 */
public class CreateSignupOrderV2Command {
	@NotNull
	private Long activityId;

	private String clientAppName;

	private String openid;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
