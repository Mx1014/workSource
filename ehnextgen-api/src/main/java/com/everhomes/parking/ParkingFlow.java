package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingFlow;
import com.everhomes.util.StringHelper;

public class ParkingFlow extends EhParkingFlow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
