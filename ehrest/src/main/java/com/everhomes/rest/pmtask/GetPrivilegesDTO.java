package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.util.StringHelper;

public class GetPrivilegesDTO {
	private List<String> privileges;

	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
