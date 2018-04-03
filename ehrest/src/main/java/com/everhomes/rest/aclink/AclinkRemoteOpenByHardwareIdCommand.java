package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkRemoteOpenByHardwareIdCommand {
	String hardwareId;

	public String getHardwareId() {
		return hardwareId;
	}

	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
