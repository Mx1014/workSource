package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * userId: 用户ID
 *
 */
public class QryCommunityUserAddressByUserIdCommand {
	
	private Long userId;

	private Long communityId;
	
	
	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}

	


	public Long getCommunityId() {
		return communityId;
	}



	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
