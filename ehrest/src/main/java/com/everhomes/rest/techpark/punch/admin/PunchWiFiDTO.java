package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 
 * <li>id:id</li>
 * <li>ssid：ssid</li>
 * <li>macAddress:mac地址</li>
 * </ul>
 */
public class PunchWiFiDTO {

    private Long id; 
    private String ssid;
    private String macAddress;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
}
