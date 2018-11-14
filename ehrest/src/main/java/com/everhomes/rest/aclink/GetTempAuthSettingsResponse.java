// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li> configs</li>
 * </ul>
 */
public class GetTempAuthSettingsResponse {
	private List<AclinkSettingDTO> configs;
	
	public List<AclinkSettingDTO> getConfigs() {
		return configs;
	}

	public void setConfigs(List<AclinkSettingDTO> configs) {
		this.configs = configs;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
