// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthSceneLimits;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class EnterprisePaymentAuthSceneLimit extends EhEnterprisePaymentAuthSceneLimits {
	
	private static final long serialVersionUID = 7840033883863092740L;

	public EnterprisePaymentAuthSceneLimit() {
		this.setHistoricalPayCount(0);
		this.setHistoricalTotalPayAmount(BigDecimal.ZERO);
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}