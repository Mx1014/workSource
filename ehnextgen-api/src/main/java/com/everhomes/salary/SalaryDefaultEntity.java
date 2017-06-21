// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryDefaultEntities;
import com.everhomes.util.StringHelper;

public class SalaryDefaultEntity extends EhSalaryDefaultEntities {
	
	private static final long serialVersionUID = -7357614545925109711L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}