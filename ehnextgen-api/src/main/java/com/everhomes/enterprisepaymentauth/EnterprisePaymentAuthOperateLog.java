// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthOperateLogs;
import com.everhomes.util.StringHelper;

public class EnterprisePaymentAuthOperateLog extends EhEnterprisePaymentAuthOperateLogs {
	
	private static final long serialVersionUID = 1027281029162712692L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}