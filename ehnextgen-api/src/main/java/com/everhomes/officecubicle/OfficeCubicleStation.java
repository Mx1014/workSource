package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleStation;
import com.everhomes.util.StringHelper;

import java.util.List;

public class OfficeCubicleStation extends EhOfficeCubicleStation {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6940036154263417250L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
