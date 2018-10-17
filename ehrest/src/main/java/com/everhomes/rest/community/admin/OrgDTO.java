package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

public class OrgDTO {
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
