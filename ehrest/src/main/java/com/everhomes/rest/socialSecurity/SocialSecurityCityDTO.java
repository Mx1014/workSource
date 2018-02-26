package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>社保城市:
 * <li>cityId: 城市id</li>
 * <li>cityName: 城市名</li>
 * </ul>
 */
public class SocialSecurityCityDTO {
	private Long cityId;
	private String cityName;
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
