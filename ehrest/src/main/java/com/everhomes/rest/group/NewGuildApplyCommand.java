// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>group_id: group_id</li>
 *     <li>name: name</li>
 *     <li>phone: phone</li>
 *     <li>email: email</li>
 *     <li>organizationName: organizationName</li>
 *     <li>registeredCapital: registeredCapital</li>
 *     <li>industryType: industryType</li>
 * </ul>
 */
public class NewGuildApplyCommand {

	private Integer namespaceId;
	private Long group_id;
	private String name;
	private String phone;
	private String email;
	private String organizationName;
	private String registeredCapital;
	private String industryType;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
