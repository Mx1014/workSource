package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * userId: 用户ID
 *
 */
public class QryCommunityUserAddressByUserIdCommand {
	
	private String contactToken;

	private Long communityId;
	
	private Long userId;

	public String getContactToken() {
		return contactToken;
	}



	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}



	public Long getCommunityId() {
		return communityId;
	}



	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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
