// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressServiceAddresses;
import com.everhomes.util.StringHelper;

public class ExpressServiceAddress extends EhExpressServiceAddresses {
	
	private static final long serialVersionUID = -205883262224150211L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}