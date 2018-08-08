// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchMonthReports;
import com.everhomes.util.StringHelper;

public class PunchMonthReport extends EhPunchMonthReports {
	
	private static final long serialVersionUID = -7151803357717706270L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}