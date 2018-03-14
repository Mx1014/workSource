// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchVacationBalanceLogs;
import com.everhomes.util.StringHelper;

public class PunchVacationBalanceLog extends EhPunchVacationBalanceLogs {
	
	private static final long serialVersionUID = 3922097877505709012L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}