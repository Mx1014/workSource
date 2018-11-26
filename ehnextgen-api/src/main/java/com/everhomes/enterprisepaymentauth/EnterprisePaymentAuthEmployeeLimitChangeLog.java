// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeeLimitChangeLogs;
import com.everhomes.util.StringHelper;

public class EnterprisePaymentAuthEmployeeLimitChangeLog extends EhEnterprisePaymentAuthEmployeeLimitChangeLogs {
	
	private static final long serialVersionUID = 6738584861723947708L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}