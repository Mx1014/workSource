// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryGroupsReportResources;
import com.everhomes.util.StringHelper;

public class SalaryGroupsReportResource extends EhSalaryGroupsReportResources {
	
	private static final long serialVersionUID = 6421249074437586656L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}