// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryEmployeesFiles;
import com.everhomes.util.StringHelper;

public class SalaryEmployeesFile extends EhSalaryEmployeesFiles {
	
	private static final long serialVersionUID = 7106861399146638642L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}