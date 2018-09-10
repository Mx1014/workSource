// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li> configs</li>
 * </ul>
 */
public class GetTempAuthSettingsResponse {
	private String configs;

	public String getConfigs() {
		return configs;
	}

	public void setConfigs(String configs) {
		this.configs = configs;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
