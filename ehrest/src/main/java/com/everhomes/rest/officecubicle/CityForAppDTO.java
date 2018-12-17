package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 城市dto
 * <li>cityId: 城市id</li>
 * <li>cityName: 城市名</li>
 * </ul>
 */
public class CityForAppDTO {
	private Long cityId;
	private String cityName;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}


}
