//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>phone: 手机</li>
 * <li>nickName: 昵称</li>
 * <li>realName: 真实姓名</li>
 * <li>gender: 性别，0未知1男2女，参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>communityName: 园区名称</li>
 * <li>organizationName: 企业名称</li>
 * <li>position: 职位</li>
 * <li>leaderFlag: 是否高管，1是0否</li>
 * <li>type: 类型，1认证2非认证3非注册</li>
 * <li>sourceFlag: 来源，1自发报名2后台录入</li>
 * <li>confirmFlag: 报名确认，1已确认0未确认</li>
 * <li>checkinFlag: 是否已签到，1是0否</li>
 * </ul>
 */
public class SignupInfoDTO {
	private Long id;
	private String phone;
	private String nickName;
	private String realName;
	private Byte gender;
	private String genderText;
	private String communityName;
	private String organizationName;
	private String position;
	private Byte leaderFlag;
	private String leaderFlagText;
	private Byte type;
	private String typeText;
	private Byte sourceFlag;
	private String sourceFlagText;
	private Byte confirmFlag;
	private String confirmFlagText;
	private Byte checkinFlag;
	private String checkinFlagText;
	
	public String getGenderText() {
		return genderText;
	}
	public void setGenderText(String genderText) {
		this.genderText = genderText;
	}
	public String getLeaderFlagText() {
		return leaderFlagText;
	}
	public void setLeaderFlagText(String leaderFlagText) {
		this.leaderFlagText = leaderFlagText;
	}
	public String getTypeText() {
		return typeText;
	}
	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}
	public String getSourceFlagText() {
		return sourceFlagText;
	}
	public void setSourceFlagText(String sourceFlagText) {
		this.sourceFlagText = sourceFlagText;
	}
	public String getConfirmFlagText() {
		return confirmFlagText;
	}
	public void setConfirmFlagText(String confirmFlagText) {
		this.confirmFlagText = confirmFlagText;
	}
	public String getCheckinFlagText() {
		return checkinFlagText;
	}
	public void setCheckinFlagText(String checkinFlagText) {
		this.checkinFlagText = checkinFlagText;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Byte getSourceFlag() {
		return sourceFlag;
	}
	public void setSourceFlag(Byte sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	public Byte getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Byte confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	public Byte getCheckinFlag() {
		return checkinFlag;
	}
	public void setCheckinFlag(Byte checkinFlag) {
		this.checkinFlag = checkinFlag;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
