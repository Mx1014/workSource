// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryGroupsFiles;
import com.everhomes.util.StringHelper;

public class SalaryGroupsFile extends EhSalaryGroupsFiles {
	
	private static final long serialVersionUID = 1302924552570910157L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}