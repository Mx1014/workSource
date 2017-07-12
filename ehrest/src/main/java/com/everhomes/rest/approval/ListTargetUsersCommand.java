package com.everhomes.rest.approval;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>ownerType：所属对象类型organization/user</li>
 * <li>ownerId：所属对象id</li>
 * <li>listType：查找哪种规则 approval/punch</li> 
 * </ul>
 */
public class ListTargetUsersCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	private String listType; 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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
 
	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	
}
