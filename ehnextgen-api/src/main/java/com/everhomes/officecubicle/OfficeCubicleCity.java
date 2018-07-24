// @formatter:off
package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleCities;
import com.everhomes.util.StringHelper;

public class OfficeCubicleCity extends EhOfficeCubicleCities {
	
	private static final long serialVersionUID = 3880931258638785152L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}