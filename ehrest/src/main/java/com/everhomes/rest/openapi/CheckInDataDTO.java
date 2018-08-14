package com.everhomes.rest.openapi;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>id：打卡id</li> 
 * <li>userId：用户id</li> 
 * <li>userName：姓名</li> 
 * <li>contactToken：手机号</li> 
 * <li>checkInDate： 打卡日期 时间戳</li> 
 * <li>checkInTime： 实际打卡时间 时间戳</li>  
 * <li>latitude： 维度</li>
 * <li>longitude： 经度</li>
 * <li>locationInfo：地点信息</li>
 * <li>wifiInfo： wifi信息</li>
 * <li>status：状态</li>
 * </ul>
 */
public class CheckInDataDTO {

	private Long id;
	private Long userId;
	private String userName;
	private String contactToken;
	private Long checkInDate; 
    private Long checkInTime; 
	private Double longitude;
	private Double latitude;
	private String locationInfo;
    private String wifiInfo;
    private String status;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Long checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Long getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Long checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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

	public String getWifiInfo() {
		return wifiInfo;
	}

	public void setWifiInfo(String wifiInfo) {
		this.wifiInfo = wifiInfo;
	}

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
 
}
