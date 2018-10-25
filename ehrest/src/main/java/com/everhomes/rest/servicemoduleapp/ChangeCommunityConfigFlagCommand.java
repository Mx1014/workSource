package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: communityId</li>
 *     <li>appSelfConfigFlag: 是否自己配置参考，0,null-否（跟随默认），1-是（自己配置），{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ChangeCommunityConfigFlagCommand {
	private Long communityId;
	private Byte appSelfConfigFlag;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Byte getAppSelfConfigFlag() {
		return appSelfConfigFlag;
	}

	public void setAppSelfConfigFlag(Byte appSelfConfigFlag) {
		this.appSelfConfigFlag = appSelfConfigFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
