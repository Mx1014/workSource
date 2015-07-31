package com.everhomes.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>cityId : 城市id</li>
 * 	<li>areaId : 区县id</li>
 *	<li>address : 地址</li>
 *	<li>communityId : 小区id</li>
 *	<li>name : 组织名</li>
 *	<li>orgType : 组织类型，详情{@link com.everhomes.organization.OrganizationType} </li>
 *	<li>parentId : 父组织id</li>
 *	<li>description : 描述</li>
 *</ul>
 *
 */
public class UserJoinOrganizationCommand {
	
	private Long cityId;
	private Long areaId;
	private String address;

	private Long communityId;
	@NotNull	
	private String name;
	@NotNull
	private String orgType;
	
	private Long parentId;
	
	private String description;

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	
	

}
