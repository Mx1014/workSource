// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeeLimitDetails;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class EnterprisePaymentAuthEmployeeLimitDetail extends EhEnterprisePaymentAuthEmployeeLimitDetails {
	
	private static final long serialVersionUID = 9127345215753715836L;

	public EnterprisePaymentAuthEmployeeLimitDetail() {
		this.setHistoricalPayCount(0);
		this.setHistoricalTotalPayAmount(BigDecimal.ZERO);
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}