// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeeLimits;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class EnterprisePaymentAuthEmployeeLimit extends EhEnterprisePaymentAuthEmployeeLimits {
	
	private static final long serialVersionUID = -3385838768284736621L;

	public EnterprisePaymentAuthEmployeeLimit() {
		this.setHistoricalPayCount(0);
		this.setHistoricalTotalPayAmount(BigDecimal.ZERO);
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}