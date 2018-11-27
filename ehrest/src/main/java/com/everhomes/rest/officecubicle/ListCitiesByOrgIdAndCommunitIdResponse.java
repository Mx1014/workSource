package com.everhomes.rest.officecubicle;

import java.util.List;

import com.everhomes.util.StringHelper;


public class ListCitiesByOrgIdAndCommunitIdResponse {
	private String defaultCity;
	private List<String> city;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getDefaultCity() {
		return defaultCity;
	}

	public void setDefaultCity(String defaultCity) {
		this.defaultCity = defaultCity;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

}
