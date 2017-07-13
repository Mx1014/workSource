// @formatter:off
package com.everhomes.authorization.zjgk;

import com.everhomes.util.StringHelper;

public class ZjgkResponse {
	private String communityName;
	private String buildingName;
	private String apartmentName;
	private String address;
	private Byte existCommunityFlag = 1;
	public Byte getExistCommunityFlag() {
		return existCommunityFlag;
	}
	public void setExistCommunityFlag(Byte existCommunityFlag) {
		this.existCommunityFlag = existCommunityFlag;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
