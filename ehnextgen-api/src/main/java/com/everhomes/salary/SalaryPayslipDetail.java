// @formatter:off
package com.everhomes.salary;

import com.everhomes.server.schema.tables.pojos.EhSalaryPayslipDetails;
import com.everhomes.util.StringHelper;

public class SalaryPayslipDetail extends EhSalaryPayslipDetails {
	
	private static final long serialVersionUID = -2881671102683661150L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}