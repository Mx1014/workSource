// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchVacationBalances;
import com.everhomes.util.StringHelper;

public class PunchVacationBalance extends EhPunchVacationBalances {
	
	private static final long serialVersionUID = 2710259387551493711L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}