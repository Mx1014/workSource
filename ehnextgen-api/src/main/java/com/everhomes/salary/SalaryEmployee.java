// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryEmployees;
import com.everhomes.util.StringHelper;

public class SalaryEmployee extends EhSalaryEmployees {
	
	private static final long serialVersionUID = 1789522900042393218L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}