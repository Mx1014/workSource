package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleChargeUsers;
import com.everhomes.util.StringHelper;

public class OfficeCubicleChargeUser extends EhOfficeCubicleChargeUsers {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4617123268581639478L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
