// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.server.schema.tables.pojos.EhAccumulationFundPayments;
import com.everhomes.util.StringHelper;

public class AccumulationFundPayment extends EhAccumulationFundPayments {
	
	private static final long serialVersionUID = -125503570601930237L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}