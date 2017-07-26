// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryEmployeePeriodVals;
import com.everhomes.util.StringHelper;

public class SalaryEmployeePeriodVal extends EhSalaryEmployeePeriodVals {
	
	private static final long serialVersionUID = -5628858815106643838L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}