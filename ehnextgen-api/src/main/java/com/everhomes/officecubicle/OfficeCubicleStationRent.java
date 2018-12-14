package com.everhomes.officecubicle;


import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleStationRent;
import com.everhomes.util.StringHelper;

public class OfficeCubicleStationRent extends EhOfficeCubicleStationRent {



	/**
	 * 
	 */
	private static final long serialVersionUID = 6892527220662895389L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
