package com.everhomes.rest.parking;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListParkingCarSeriesResponse {

	private Long nextPageAnchor;
	
	@ItemType(ParkingCarSerieDTO.class)
	private List<ParkingCarSerieDTO> carSeries;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ParkingCarSerieDTO> getCarSeries() {
		return carSeries;
	}

	public void setCarSeries(List<ParkingCarSerieDTO> carSeries) {
		this.carSeries = carSeries;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
