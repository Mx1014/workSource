// @formatter:off
package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2PriceRules;
import com.everhomes.util.StringHelper;

public class Rentalv2PriceRule extends EhRentalv2PriceRules {
	
	private static final long serialVersionUID = 5296764597917186698L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}