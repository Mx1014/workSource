package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contactToken: 电话号码</li>
 * <li>communityId: 小区id</li>
 * <li>userId: 用户id</li>
 * </ul>
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
