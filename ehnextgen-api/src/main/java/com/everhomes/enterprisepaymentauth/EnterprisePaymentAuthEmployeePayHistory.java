// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeePayHistories;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class EnterprisePaymentAuthEmployeePayHistory extends EhEnterprisePaymentAuthEmployeePayHistories {
	
	private static final long serialVersionUID = -8605663680158075074L;

	public EnterprisePaymentAuthEmployeePayHistory() {
		this.setUsedAmount(BigDecimal.ZERO);
		this.setPayCount(0);
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}