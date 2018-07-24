// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryPayslips;
import com.everhomes.util.StringHelper;

public class SalaryPayslip extends EhSalaryPayslips {
	
	private static final long serialVersionUID = -8987387872038514646L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}