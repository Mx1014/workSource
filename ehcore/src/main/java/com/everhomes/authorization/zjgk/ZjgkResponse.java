// @formatter:off
package com.everhomes.authorization.zjgk;

import com.everhomes.util.StringHelper;

public class ZjgkResponse {
	public static final byte EXIST_COMMUNITY = 1;//存在一个园区
	public static final byte NOT_EXIST_COMMUNITY = 0;//不存在园区
	public static final byte MULTI_COMMUNITY = -1;//多个园区
	
	private String communityName;
	private String buildingName;
	private String apartmentName;
	private String address;
	private Long addressId;
    private String fullAddress;
    private Integer userCount;
    private Byte existCommunityFlag = EXIST_COMMUNITY;
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
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public Integer getUserCount() {
		return userCount;
	}
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
