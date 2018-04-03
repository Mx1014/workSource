//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>realName: 真实姓名</li>
 * <li>gender: 性别，0未知1男2女，参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>communityName: 园区名称</li>
 * <li>organizationName: 企业名称</li>
 * <li>position: 职位</li>
 * <li>leaderFlag: 是否高管，1是0否</li>
 * <li>checkinFlag: 是否签到，1是0否</li>
 * <li>email: 邮箱</li>
 * </ul>
 */
public class UpdateSignupInfoCommand {
	private Long id;
	private String realName;
	private Byte gender;
	private String communityName;
	private String organizationName;
	private String position;
	private Byte leaderFlag;
	private Byte checkinFlag;
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Byte getCheckinFlag() {
		return checkinFlag;
	}

	public void setCheckinFlag(Byte checkinFlag) {
		this.checkinFlag = checkinFlag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
