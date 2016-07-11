package com.everhomes.techpark.park;

import com.everhomes.server.schema.tables.pojos.EhParkCharge;
import com.everhomes.util.StringHelper;

public class ParkCharge extends EhParkCharge{

	/**
	 * @author xiongying
	 */
	private static final long serialVersionUID = 5395590496876541538L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
