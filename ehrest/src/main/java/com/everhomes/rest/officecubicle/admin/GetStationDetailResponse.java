package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeRentOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 */
public class GetStationDetailResponse {

	private List<StationDTO> station;





	public List<StationDTO> getStation() {
		return station;
	}





	public void setStation(List<StationDTO> station) {
		this.station = station;
	}





	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
