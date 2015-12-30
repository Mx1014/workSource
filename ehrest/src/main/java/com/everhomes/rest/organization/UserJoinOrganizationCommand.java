package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>userJoin : 是否将当前用户添加进组织成员,true-加入,false-不加入,只创建组织</li>
 * 	<li>memberType : 加入组织成员组类型,默认为manager,详情:{@link com.everhomes.rest.organization.OrganizationMemberGroupType}
 * 	<li>cityId : 城市id</li>
 * 	<li>areaId : 区县id</li>
 *	<li>address : 地址</li>
 *	<li>communityId : 小区id</li>
 *	<li>name : 组织名</li>
 *	<li>orgType : 组织类型，详情{@link com.everhomes.rest.organization.OrganizationType} </li>
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
	@NotNull
	private boolean userJoin;
	private String memberType;

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
	
	public boolean isUserJoin() {
		return userJoin;
	}

	public void setUserJoin(boolean userJoin) {
		this.userJoin = userJoin;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	
	

}
