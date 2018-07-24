// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>cities: 参考 {@link com.everhomes.rest.officecubicle.admin.CityDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListCitiesResponse {

    private List<CityDTO> cities;

    private Long nextPageAnchor;

    public ListCitiesResponse(List<CityDTO> cities) {
    	this.cities=cities;
	}

	public ListCitiesResponse() {
	}

	public List<CityDTO> getCities() {
        return cities;
    }

    public void setCities(List<CityDTO> cities) {
        this.cities = cities;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
