// @formatter:off
package com.everhomes.print;

import com.everhomes.server.schema.tables.pojos.EhSiyinPrintOrders;
import com.everhomes.util.StringHelper;

public class SiyinPrintOrder extends EhSiyinPrintOrders {
	
	private static final long serialVersionUID = 1005131533931753717L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}