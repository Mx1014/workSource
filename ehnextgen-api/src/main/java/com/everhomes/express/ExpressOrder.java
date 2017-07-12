// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressOrders;
import com.everhomes.util.StringHelper;

public class ExpressOrder extends EhExpressOrders {
	
	private static final long serialVersionUID = -4795356810851460784L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}