package com.everhomes.organization;

import com.everhomes.util.StringHelper;


/**
 * 
 * <ul>
 * <li>id : 组织id</li>
 * <li>name : 组织名称</li>
 * <li>organizationType : 组织类型</li>
 *	</ul>
 */
public class OrganizationSimpleDTO {

	private Long id;

	private String name;

	private String organizationType;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	




}
