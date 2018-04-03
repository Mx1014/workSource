package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>hardwareId: 门禁 mac 地址</li>
 * <li>currentTime: 门禁设备当前时间</li>
 * </ul>
 * @author janson
 *
 */
public class AclinkSyncTimerCommand {
    @NotNull
    String hardwareId;
    Long currentTime;

    public String getHardwareId() {
		return hardwareId;
	}

	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}

	public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
