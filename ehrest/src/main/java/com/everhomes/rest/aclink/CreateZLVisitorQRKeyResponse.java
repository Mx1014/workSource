// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>MacAddress:mac地址</li>
 * <li>qrCode:二维码字符串</li>
 * </ul>
 */
public class CreateZLVisitorQRKeyResponse {
	private String MacAddress;
	private String qrCode;
	

	public String getMacAddress() {
		return MacAddress;
	}
	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
