package com.everhomes.rest.wifi;

/**
 * <ul>
 * <li>id: wifi ID</li>
 * <li>ssid: 网络名称</li>
 * </ul>
 */
public class EditWifiSettingCommand {
	private Long id;
	private java.lang.String   ssid;
	

	public java.lang.String getSsid() {
		return ssid;
	}

	public void setSsid(java.lang.String ssid) {
		this.ssid = ssid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
