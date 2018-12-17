// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>authId:授权id</li>
 * <li>keySecret: aesUserKey</li>
 * <li>macAddress: 门禁mac地址</li>
 * </ul>
 */
public class AclinkAppUserKeyDTO {
	Long authId;
	String keySecret;
	String macAddress;
	
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
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
