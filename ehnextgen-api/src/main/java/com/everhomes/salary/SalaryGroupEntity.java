// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryGroupEntities;
import com.everhomes.util.StringHelper;

public class SalaryGroupEntity extends EhSalaryGroupEntities {
	
	private static final long serialVersionUID = -4759610341287325926L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}