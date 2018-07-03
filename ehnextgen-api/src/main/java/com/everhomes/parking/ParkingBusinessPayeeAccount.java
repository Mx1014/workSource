// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingBusinessPayeeAccounts;
import com.everhomes.util.StringHelper;

public class ParkingBusinessPayeeAccount extends EhParkingBusinessPayeeAccounts {
	
	private static final long serialVersionUID = -1980643928842774121L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}