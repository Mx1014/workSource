package com.everhomes.rest.wifi;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flag: 1表示成功  ,0 失败 com.everhomes.rest.wifi.VerifyWifiStatus</li>
 * </ul>
 */
public class VerifyWifiDTO {
	private byte status;

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
