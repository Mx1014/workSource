package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 岗位id </li>
 * <li>name: 岗位名称 </li>
 * <li>ownerType : 范围类型</li>
 * <li>ownerId: 范围id</li>
 * <li>discription : 描述</li>
 * </ul>
 */
public class OrganizationJobPositionDTO {

	private Long id;
	private String ownerType;
	private Long ownerId;
	private String name;
	private String discription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
