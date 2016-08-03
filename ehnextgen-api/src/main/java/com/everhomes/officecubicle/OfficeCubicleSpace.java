package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSpaces;
import com.everhomes.util.StringHelper;

public class OfficeCubicleSpace extends EhOfficeCubicleSpaces {

	/**
	 * 
	 */
	private static final long serialVersionUID = 526862315847282405L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
