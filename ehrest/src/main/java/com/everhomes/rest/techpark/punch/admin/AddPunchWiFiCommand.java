package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *  
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>targetType: 填organization/user</li>
 * <li>targetId：对应设置目标的id比如机构比如人的id</li>
 * <li>ssid：ssid</li>
 * <li>macAddress:mac地址</li>
 * </ul>
 */
public class AddPunchWiFiCommand {


	private String ownerType; 
	private Long ownerId;
	private String targetType;
	private Long targetId; 
    private String ssid;
    private String macAddress;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	
}
