package com.everhomes.techpark.park;

import com.everhomes.server.schema.tables.pojos.EhParkApplyCard;
import com.everhomes.util.StringHelper;

public class ParkApplyCard extends EhParkApplyCard {

	private static final long serialVersionUID = 1365120263080104171L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
