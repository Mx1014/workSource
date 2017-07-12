package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingStatistics;
import com.everhomes.util.StringHelper;

public class ParkingStatistic extends EhParkingStatistics {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
