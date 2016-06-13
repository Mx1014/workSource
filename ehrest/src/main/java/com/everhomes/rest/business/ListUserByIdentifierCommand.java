package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>identifier:用户手机</li>
 * </ul>
 *
 */
public class ListUserByIdentifierCommand {
	private String identifier;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
