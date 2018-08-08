// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>IpAddress：ip地址</li>
 * <li>uuid：配对码</li>
 * </ul>
 */
public class PairIpadCommand {
	String ipAddress;
	String uuid;

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
