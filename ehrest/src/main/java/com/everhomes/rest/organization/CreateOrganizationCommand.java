// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId：父机构id。没有填0</li>
 * <li>name：名称</li>
 * <li>address：地址</li>
 * <li>groupType：机构类型{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 * </ul>
 */
public class CreateOrganizationCommand {
	private Long    parentId;
	@NotNull
	private String  name;
	
	private String address;
	
	private String groupType;
	
	
	public CreateOrganizationCommand() {
    }
	
	
	public Long getParentId() {
		return parentId;
	}


	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getGroupType() {
		return groupType;
	}


	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
