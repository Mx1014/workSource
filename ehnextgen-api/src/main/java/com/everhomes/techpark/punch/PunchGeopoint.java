package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchGeopoints;
import com.everhomes.util.StringHelper;

public class PunchGeopoint extends EhPunchGeopoints{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7481191310582043573L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
