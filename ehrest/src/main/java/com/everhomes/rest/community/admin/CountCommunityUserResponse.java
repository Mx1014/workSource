package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>communityUsers: 园区用户数</li>
 *  <li>authUsers: 园区认证用户数</li>
 *  <li>notAuthUsers: 园区非认证用户数</li>
 * </ul>
 */
public class CountCommunityUserResponse {
	
	private Integer communityUsers;
	
	private Integer authUsers;
	
	private Integer notAuthUsers;

	public Integer getCommunityUsers() {
		return communityUsers;
	}

	public void setCommunityUsers(Integer communityUsers) {
		this.communityUsers = communityUsers;
	}

	public Integer getAuthUsers() {
		return authUsers;
	}

	public void setAuthUsers(Integer authUsers) {
		this.authUsers = authUsers;
	}

	public Integer getNotAuthUsers() {
		return notAuthUsers;
	}

	public void setNotAuthUsers(Integer notAuthUsers) {
		this.notAuthUsers = notAuthUsers;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
