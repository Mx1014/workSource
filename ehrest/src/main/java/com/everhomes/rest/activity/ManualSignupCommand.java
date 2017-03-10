//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>activityId: 活动id</li>
 * <li>phone: 手机</li>
 * <li>realName: 真实姓名</li>
 * <li>gender: 性别，0未知1男2女，参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>communityName: 园区名称</li>
 * <li>organizationName: 企业名称</li>
 * <li>position: 职位</li>
 * <li>leaderFlag: 是否高管，1是0否</li>
 * </ul>
 */
public class ManualSignupCommand {
	private Long activityId;
	private String phone;
	private String realName;
	private Byte gender;
	private String communityName;
	private String organizationName;
	private String position;
	private Byte leaderFlag;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Byte getLeaderFlag() {
		return leaderFlag;
	}

	public void setLeaderFlag(Byte leaderFlag) {
		this.leaderFlag = leaderFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
