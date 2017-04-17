// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressServiceAddresses;
import com.everhomes.util.StringHelper;

public class ExpressServiceAddresse extends EhExpressServiceAddresses {
	
	private static final long serialVersionUID = -5248787966268975534L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}