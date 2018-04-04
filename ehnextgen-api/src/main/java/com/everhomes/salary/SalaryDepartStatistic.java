// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryDepartStatistics;
import com.everhomes.util.StringHelper;

public class SalaryDepartStatistic extends EhSalaryDepartStatistics {
	
	private static final long serialVersionUID = 2632569652370719878L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}