// @formatter:off
package com.everhomes.welfare;

import com.everhomes.server.schema.tables.pojos.EhWelfareCoupons;
import com.everhomes.util.StringHelper;

public class WelfareCoupon extends EhWelfareCoupons {
	
	private static final long serialVersionUID = 7061951452719220533L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}