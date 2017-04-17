// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressAddresses;
import com.everhomes.util.StringHelper;

public class ExpressAddresse extends EhExpressAddresses {
	
	private static final long serialVersionUID = -4693940026372791675L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}