// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

public class SetTempAuthSettingsCommand {
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
