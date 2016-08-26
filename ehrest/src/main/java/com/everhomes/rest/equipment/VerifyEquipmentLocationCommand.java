package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>qrCodeToken: 设备二维码token</li>
 *  <li>longitude: 经度</li>
 *  <li>latitude: 纬度</li>
 * </ul>
 */
public class VerifyEquipmentLocationCommand {
	
	private String qrCodeToken;
	
	private Double longitude;
	
    private Double latitude;
    

	public String getQrCodeToken() {
		return qrCodeToken;
	}

	public void setQrCodeToken(String qrCodeToken) {
		this.qrCodeToken = qrCodeToken;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
