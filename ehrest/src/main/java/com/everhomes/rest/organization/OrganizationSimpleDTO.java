package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * 
 * <ul>
 * <li>id : 组织id</li>
 * <li>name : 组织名称</li>
 * <li>organizationType : 组织类型</li>
 * 物业或业委 需要附加字段 :
 * <li>communityId : 小区id</li>
 * <li>communityName : 小区name</li> 
 *	</ul>
 */
public class OrganizationSimpleDTO {

	private Long id;

	private String name;

	private String organizationType;
	
	private Long communityId;
	
	private String communityName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}
	
	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	




}
