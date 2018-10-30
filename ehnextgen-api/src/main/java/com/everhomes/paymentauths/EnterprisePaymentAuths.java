package com.everhomes.paymentauths;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuths;
import com.everhomes.util.StringHelper;

public class EnterprisePaymentAuths extends EhEnterprisePaymentAuths {


    /**
	 * 
	 */
	private static final long serialVersionUID = -196875389370792407L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
