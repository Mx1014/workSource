package com.everhomes.rest.wifi;

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
	
}
