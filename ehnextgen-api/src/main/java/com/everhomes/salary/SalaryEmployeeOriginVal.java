// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryEmployeeOriginVals;
import com.everhomes.util.StringHelper;

public class SalaryEmployeeOriginVal extends EhSalaryEmployeeOriginVals {
	
	private static final long serialVersionUID = -7865999932384876468L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}