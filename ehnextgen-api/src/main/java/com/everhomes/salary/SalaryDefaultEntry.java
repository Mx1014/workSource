// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryDefaultEntries;
import com.everhomes.util.StringHelper;

public class SalaryDefaultEntry extends EhSalaryDefaultEntries {
	
	private static final long serialVersionUID = -7357614545925109711L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}