// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

public class DeleteAddressArrangementCommand {
	
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
