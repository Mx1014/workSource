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

	private Double latigtue;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatigtue() {
		return latigtue;
	}

	public void setLatigtue(Double latigtue) {
		this.latigtue = latigtue;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
