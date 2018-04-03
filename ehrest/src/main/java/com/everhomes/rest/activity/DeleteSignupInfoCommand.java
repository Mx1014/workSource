//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * </ul>
 */
public class DeleteSignupInfoCommand {
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
