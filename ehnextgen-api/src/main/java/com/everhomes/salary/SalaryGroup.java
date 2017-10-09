// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryGroups;
import com.everhomes.util.StringHelper;

public class SalaryGroup extends EhSalaryGroups {
	
	private static final long serialVersionUID = -3625373362904304648L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}