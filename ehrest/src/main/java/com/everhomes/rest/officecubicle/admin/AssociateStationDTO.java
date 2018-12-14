// @formatter:off
package com.everhomes.rest.officecubicle.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 */
public class AssociateStationDTO {
    private Long stationId;
    private String stationName;

	public Long getStationId() {
		return stationId;
	}



	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}



	public String getStationName() {
		return stationName;
	}



	public void setStationName(String stationName) {
		this.stationName = stationName;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
