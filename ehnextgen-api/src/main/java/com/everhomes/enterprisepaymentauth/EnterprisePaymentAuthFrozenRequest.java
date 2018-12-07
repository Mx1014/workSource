// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthFrozenRequests;
import com.everhomes.util.StringHelper;

public class EnterprisePaymentAuthFrozenRequest extends EhEnterprisePaymentAuthFrozenRequests {
	
	private static final long serialVersionUID = 8709427749583029027L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}