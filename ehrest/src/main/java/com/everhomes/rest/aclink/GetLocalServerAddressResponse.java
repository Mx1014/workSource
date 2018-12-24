// @formatter:off
package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>ipAddress:内网ip地址+端口</li>
 * </ul>
 *
 */
public class GetLocalServerAddressResponse {
	private String ipAddress;
	private String logoUrl;
	

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
}
