// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>macAddress:mac地址</li>
 * <li>qrCode:二维码字符串</li>
 * <li>phone:电话</li>
 * <li>userName:姓名</li>
 * <li>keySecret:钥匙(用于生成防截图二维码)</li>
 * <li>authId:授权id</li>
 * </ul>
 */
public class CreateZLVisitorQRKeyResponse {
	private String MacAddress;//待对接方上线,改为macAddress
	private String qrCode;
	private String phone;
	private String userName;
	private String keySecret;
	private Long authId;
	
	public String getMacAddress() {
		return MacAddress;
	}
	public void setMacAddress(String macAddress) {
		this.MacAddress = macAddress;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getKeySecret() {
		return keySecret;
	}
	public void setKeySecret(String keySecret) {
		this.keySecret = keySecret;
	}
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
