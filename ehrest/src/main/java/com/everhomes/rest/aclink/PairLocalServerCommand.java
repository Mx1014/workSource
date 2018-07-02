// @formatter: off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>uuid：配对码</li>
 * <li>ipAddress：ip地址</li>
 * <li>version：版本号</li>
 * </ul>
 */
public class PairLocalServerCommand {
	String uuid;
	String ipAddress;
	String version;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
