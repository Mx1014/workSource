// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingHubs;
import com.everhomes.util.StringHelper;

public class ParkingHub extends EhParkingHubs {
	
	private static final long serialVersionUID = -3541503596705737035L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}