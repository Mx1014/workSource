package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>longitude: 经度</li>
 *     <li>latigtue: 纬度</li>
 * </ul>
 */
public class FindNearbyMixCommunityCommand {

	private Double longitude;

	private Double latitude;

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
