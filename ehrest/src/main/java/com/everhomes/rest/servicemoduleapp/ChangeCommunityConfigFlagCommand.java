package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: communityId</li>
 *     <li>selfConfigFlag: 是否自己配置参考，{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ChangeCommunityConfigFlagCommand {
	private Long communityId;
	private Byte selfConfigFlag;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Byte getSelfConfigFlag() {
		return selfConfigFlag;
	}

	public void setSelfConfigFlag(Byte selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
