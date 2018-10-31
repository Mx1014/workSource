package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：企业Id 必填</li>
 * <li>latitude: 坐标纬度 必填</li>
 * <li>longitude： 坐标经度 必填</li>
 * <li>locationInfo： 地理位置信息 必填</li>
 * <li>wifiInfo： wifi信息 非必填</li>
 * <li>imgUri：照片uri 必填</li>
 * <li>createType： 创建类型 非必填 参考{@link com.everhomes.rest.techpark.punch.CreateType}</li>
 * </ul>
 */
public class GoOutPunchClockCommand {

	private Long organizationId;
	private String identification;
	private Double latitude;
	private Double longitude;
	private String locationInfo;
	private String wifiInfo;
	private String imgUri;
	private Byte createType;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getCreateType() {
		return createType;
	}

	public void setCreateType(Byte createType) {
		this.createType = createType;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getWifiInfo() {
		return wifiInfo;
	}

	public void setWifiInfo(String wifiInfo) {
		this.wifiInfo = wifiInfo;
	}
}
