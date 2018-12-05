// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeePaySceneHistories;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class EnterprisePaymentAuthEmployeePaySceneHistory extends EhEnterprisePaymentAuthEmployeePaySceneHistories {
	
	private static final long serialVersionUID = 7980325202128626321L;

	public EnterprisePaymentAuthEmployeePaySceneHistory() {
		this.setUsedAmount(BigDecimal.ZERO);
		this.setPayCount(0);
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}