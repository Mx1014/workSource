package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId：企业Id</li> 
 * <li>identification: 打卡唯一标识 必填</li>
 * <li>locationInfo： 地理位置信息 选填</li>
 * <li>wifiInfo： wifi信息 选填</li>
 * <li>createType： 创建类型 参考{@link com.everhomes.rest.techpark.punch.CreateType}</li>
 * </ul>
 */
public class ThirdPartPunchClockCommand {

	private Long enterpriseId; 
	private String identification;
    private String wifiInfo;
    private String locationInfo;  
	private Byte createType;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getWifiInfo() {
		return wifiInfo;
	}
	public void setWifiInfo(String wifiInfo) {
		this.wifiInfo = wifiInfo;
	}
	public String getLocationInfo() {
		return locationInfo;
	}
	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}
	public Byte getCreateType() {
		return createType;
	}
	public void setCreateType(Byte createType) {
		this.createType = createType;
	}
	

}
