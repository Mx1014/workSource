// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>name: 名称</li>
 * </ul>
 */
public class GetClubPlaceholderNameResponse {
	private String name;

	public GetClubPlaceholderNameResponse() {
		super();
	}

	public GetClubPlaceholderNameResponse(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
