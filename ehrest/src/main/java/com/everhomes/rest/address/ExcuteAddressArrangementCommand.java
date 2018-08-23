package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id:拆分合并计划id</li>
 * </ul>
 */
public class ExcuteAddressArrangementCommand {
	
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
