// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressPayeeAccounts;
import com.everhomes.util.StringHelper;

public class ExpressPayeeAccount extends EhExpressPayeeAccounts {
	
	private static final long serialVersionUID = 3442765362622263240L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}