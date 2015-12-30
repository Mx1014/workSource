package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

public class ListUserRelatedOrganizationsCommand {
	
	private String organiztionType;

	public String getOrganiztionType() {
		return organiztionType;
	}

	public void setOrganiztionType(String organiztionType) {
		this.organiztionType = organiztionType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
