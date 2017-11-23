// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>name: 楼栋名称</li>
 * <li>aliasName: 楼栋简称</li>
 * <li>address: 地址</li>
 * <li>latitudeLongitude: 经纬度</li>
 * <li>trafficDescription: 交通说明</li>
 * <li>areaSize: 面积（平米）</li>
 * <li>contactor: 联系人</li>
 * <li>phone: 联系电话</li>
 * <li>description: 楼栋介绍</li>
 * <li>namespaceBuildingType: 数据来源第三方 参考{@link com.everhomes.rest.address.NamespaceBuildingType}</li>
 * <li>namespaceBuildingToken: 在第三方的唯一标识</li>
 * </ul>
 */
public class ImportBuildingDataDTO {
	private String name;
	private String aliasName;
	private String address;
	private String longitudeLatitude;
	private String trafficDescription;
	private String areaSize;
	private String contactor;
	private String phone;
	private String description;
	private String namespaceBuildingType;
	private String namespaceBuildingToken;

	public String getNamespaceBuildingToken() {
		return namespaceBuildingToken;
	}

	public void setNamespaceBuildingToken(String namespaceBuildingToken) {
		this.namespaceBuildingToken = namespaceBuildingToken;
	}

	public String getNamespaceBuildingType() {
		return namespaceBuildingType;
	}

	public void setNamespaceBuildingType(String namespaceBuildingType) {
		this.namespaceBuildingType = namespaceBuildingType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitudeLatitude() {
		return longitudeLatitude;
	}

	public void setLongitudeLatitude(String longitudeLatitude) {
		this.longitudeLatitude = longitudeLatitude;
	}

	public String getTrafficDescription() {
		return trafficDescription;
	}

	public void setTrafficDescription(String trafficDescription) {
		this.trafficDescription = trafficDescription;
	}

	public String getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(String areaSize) {
		this.areaSize = areaSize;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
