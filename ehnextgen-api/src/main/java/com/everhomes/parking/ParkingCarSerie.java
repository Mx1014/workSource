package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingCarSeries;
import com.everhomes.util.StringHelper;

public class ParkingCarSerie extends EhParkingCarSeries{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
