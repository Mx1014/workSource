// @formatter:off
package com.everhomes.rest.openapi.jindi;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>金地同步数据的用户数据
 * <li>: </li>
 * </ul>
 */
public class JindiUserDTO extends JindiDataDTO {
	private Long id;
	private String nickName;
	private String gender;
	private String birthday;
	private String phone;
	private String organizationId;
	private String organizationName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
