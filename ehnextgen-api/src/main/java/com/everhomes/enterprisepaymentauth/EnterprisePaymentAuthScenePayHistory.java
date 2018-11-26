// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthScenePayHistories;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class EnterprisePaymentAuthScenePayHistory extends EhEnterprisePaymentAuthScenePayHistories {
	
	private static final long serialVersionUID = 4793070826573647902L;

	public EnterprisePaymentAuthScenePayHistory() {
		this.setUsedAmount(BigDecimal.ZERO);
		this.setPayCount(0);
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}