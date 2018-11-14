package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId：企业Id</li>
 * <li>punchType：0上班 1下班 这里只有上班或者下班</li>
 * <li>latitude: 坐标纬度</li>
 * <li>longitude： 坐标经度</li>
 * <li>wifiMac： wifiMac地址信息</li>
 * <li>createType： 创建类型 参考{@link com.everhomes.rest.techpark.punch.CreateType}</li>
 * </ul>
 */
public class PunchClockCommand {

	private Long enterpriseId;
	private Byte punchType;
	private String identification;
	private Double latitude;
	private Double longitude;
	private String wifiMac;
	private Byte createType;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getWifiMac() {
		return wifiMac;
	}

	public void setWifiMac(String wifiMac) {
		this.wifiMac = wifiMac;
	}

	public Byte getPunchType() {
		return punchType;
	}

	public void setPunchType(Byte punchType) {
		this.punchType = punchType;
	}

	public Byte getCreateType() {
		return createType;
	}

	public void setCreateType(Byte createType) {
		this.createType = createType;
	}

}
