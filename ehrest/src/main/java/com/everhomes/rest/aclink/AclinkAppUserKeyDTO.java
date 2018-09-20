// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>userId:用户id</li>
 * <li>keySecret: aesUserKey</li>
 * <li>Mac: 门禁mac地址</li>
 * </ul>
 */
public class AclinkAppUserKeyDTO {
	Long userId;
	String keySecret;
	String Mac;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getKeySecret() {
		return keySecret;
	}
	public void setKeySecret(String keySecret) {
		this.keySecret = keySecret;
	}
	public String getMac() {
		return Mac;
	}
	public void setMac(String mac) {
		Mac = mac;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
