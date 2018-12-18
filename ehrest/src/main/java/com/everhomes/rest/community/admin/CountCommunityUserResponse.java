package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityUsers: 园区用户数</li>
 *     <li>authUsers: 园区认证用户数</li>
 *     <li>notAuthUsers: 园区非认证用户数</li>
 *     <li>authingUsers: authingUsers</li>
 *     <li>wxUserCount: wxUserCount</li>
 *     <li>alipayUserCount: alipayUserCount</li>
 *     <li>appUserCount: appUserCount</li>
 *     <li>maleCount: maleCount</li>
 *     <li>femaleCount: femaleCount</li>
 * </ul>
 */
public class CountCommunityUserResponse {

	private Integer communityUsers;

	private Integer authUsers;

	private Integer notAuthUsers;

	private Integer authingUsers;

	private Integer wxUserCount;

	private Integer alipayUserCount;

	private Integer appUserCount;

	private Integer maleCount;

	private Integer femaleCount;

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

	public Integer getWxUserCount() {
		return wxUserCount;
	}

	public void setWxUserCount(Integer wxUserCount) {
		this.wxUserCount = wxUserCount;
	}

	public Integer getAlipayUserCount() {
		return alipayUserCount;
	}

	public void setAlipayUserCount(Integer alipayUserCount) {
		this.alipayUserCount = alipayUserCount;
	}

	public Integer getAppUserCount() {
		return appUserCount;
	}

	public void setAppUserCount(Integer appUserCount) {
		this.appUserCount = appUserCount;
	}

	public Integer getAuthingUsers() {
		return authingUsers;
	}

	public void setAuthingUsers(Integer authingUsers) {
		this.authingUsers = authingUsers;
	}

	public Integer getMaleCount() {
		return maleCount;
	}

	public void setMaleCount(Integer maleCount) {
		this.maleCount = maleCount;
	}

	public Integer getFemaleCount() {
		return femaleCount;
	}

	public void setFemaleCount(Integer femaleCount) {
		this.femaleCount = femaleCount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
