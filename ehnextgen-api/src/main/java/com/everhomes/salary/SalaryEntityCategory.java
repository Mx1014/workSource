// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryEntityCategories;
import com.everhomes.util.StringHelper;

public class SalaryEntityCategory extends EhSalaryEntityCategories {
	
	private static final long serialVersionUID = -7300324665320042038L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}