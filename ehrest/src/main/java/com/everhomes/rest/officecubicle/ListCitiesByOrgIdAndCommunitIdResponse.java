package com.everhomes.rest.officecubicle;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>defaultCity:默认城市</li>
 * <li>city:城市列表 List</li>
* </ul>
*/
public class ListCitiesByOrgIdAndCommunitIdResponse {
	private CityForAppDTO defaultCity;
	private List<CityForAppDTO> city;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public CityForAppDTO getDefaultCity() {
		return defaultCity;
	}



	public void setDefaultCity(CityForAppDTO defaultCity) {
		this.defaultCity = defaultCity;
	}



	public List<CityForAppDTO> getCity() {
		return city;
	}

	public void setCity(List<CityForAppDTO> city) {
		this.city = city;
	}



}
