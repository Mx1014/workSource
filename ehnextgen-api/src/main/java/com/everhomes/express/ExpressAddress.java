// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressAddresses;
import com.everhomes.util.StringHelper;

public class ExpressAddress extends EhExpressAddresses {
	
	private static final long serialVersionUID = -8644006008044405824L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}