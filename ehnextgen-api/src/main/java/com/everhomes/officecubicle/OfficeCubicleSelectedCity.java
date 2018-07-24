// @formatter:off
package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSelectedCities;
import com.everhomes.util.StringHelper;

public class OfficeCubicleSelectedCity extends EhOfficeCubicleSelectedCities {
	
	private static final long serialVersionUID = 3399736160698660540L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}