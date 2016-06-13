package com.everhomes.rest.wifi;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.wifi.WifiOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>ssid: 网络名称</li>
 * </ul>
 */
public class CreateWifiSettingCommand {
	private java.lang.String   ownerType;
	private java.lang.Long     ownerId;
	private java.lang.String   ssid;
	
	public java.lang.String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(java.lang.String ownerType) {
		this.ownerType = ownerType;
	}
	public java.lang.Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(java.lang.Long ownerId) {
		this.ownerId = ownerId;
	}
	public java.lang.String getSsid() {
		return ssid;
	}
	public void setSsid(java.lang.String ssid) {
		this.ssid = ssid;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
