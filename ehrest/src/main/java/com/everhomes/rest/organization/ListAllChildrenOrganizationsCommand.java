package com.everhomes.rest.organization;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id : 机构id</li>
 * <li>groupTypes : 机构类型 参考{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 *</ul>
 *
 */
public class ListAllChildrenOrganizationsCommand {
	
	@NotNull
	private Long id;
	
	@ItemType(String.class)
	private List<String> groupTypes;
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public List<String> getGroupTypes() {
		return groupTypes;
	}



	public void setGroupTypes(List<String> groupTypes) {
		this.groupTypes = groupTypes;
	}



	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
