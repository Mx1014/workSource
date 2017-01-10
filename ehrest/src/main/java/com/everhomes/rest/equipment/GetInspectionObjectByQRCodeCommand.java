package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>qrCodeToken: 设备二维码token</li>
 * </ul>
 */
public class GetInspectionObjectByQRCodeCommand {

	private String qrCodeToken;

	public String getQrCodeToken() {
		return qrCodeToken;
	}

	public void setQrCodeToken(String qrCodeToken) {
		this.qrCodeToken = qrCodeToken;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
