package com.everhomes.rest.paymentauths;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>orgnazitionId: 公司ID</li>
 *     <li>appId: 应用ID</li>
 *     <li>userId: 用户ID</li>
 * </ul>
 */
public class CheckUserAuthsCommand {
	private Long orgnazitionId;
	private Long appId;
	private Long userId;


	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getOrgnazitionId() {
		return orgnazitionId;
	}

	public void setOrgnazitionId(Long orgnazitionId) {
		this.orgnazitionId = orgnazitionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
