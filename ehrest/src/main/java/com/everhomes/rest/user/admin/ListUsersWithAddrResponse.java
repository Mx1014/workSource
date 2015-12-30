package com.everhomes.rest.user.admin;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;



/**
 * <ul>
 * <li>id: Id</li>
 * <li>accountName: 用户名</li>
 * <li>cellPhone: 用户电话号码</li>
 * <li>cityName: 城市名称</li>
 * <li>areaName: 区域名称</li>
 * <li>communityName: 小区名称</li>
 * <li>buildingName: 家庭地址楼栋号</li>
 * <li>apartmentName: 家庭地址门牌号</li>
 * <li>addressStatus: 地址状态, {@link com.everhomes.rest.address.AddressAdminStatus}</li>
 * <li>apartmentStatus: 公寓状态</li>
 * <li>familyName: 家庭名称</li>
 * <li>cellPhoneNumberLocation: 手机号码归属地</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */

public class ListUsersWithAddrResponse {
	
	private Long id;
	
	private String nickName;
	
	private String cellPhone;
	
	private String cityName;
	
	private String areaName;
	
	private String communityName;
	
	private String buildingName;
	
	private String apartmentName;
	
	private Byte addressStatus;
	
	private Byte apartmentStatus;
	
	private String familyName;
	
	private String cellPhoneNumberLocation;
	
	private Timestamp createTime;

	public ListUsersWithAddrResponse(){
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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

	public Byte getAddressStatus() {
		return addressStatus;
	}

	public void setAddressStatus(Byte addressStatus) {
		this.addressStatus = addressStatus;
	}

	public Byte getApartmentStatus() {
		return apartmentStatus;
	}

	public void setApartmentStatus(Byte apartmentStatus) {
		this.apartmentStatus = apartmentStatus;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getCellPhoneNumberLocation() {
		return cellPhoneNumberLocation;
	}

	public void setCellPhoneNumberLocation(String cellPhoneNumberLocation) {
		this.cellPhoneNumberLocation = cellPhoneNumberLocation;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	 @Override
	 public String toString(){
		 return StringHelper.toJsonString(this);
	        
	 }


}
