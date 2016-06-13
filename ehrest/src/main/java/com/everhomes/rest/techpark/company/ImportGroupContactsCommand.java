package com.everhomes.rest.techpark.company;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>ownerId: 组id</li>
* </ul>
*/
public class ImportGroupContactsCommand {
	@NotNull
	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
