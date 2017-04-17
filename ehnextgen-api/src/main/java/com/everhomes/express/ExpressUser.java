// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressUsers;
import com.everhomes.util.StringHelper;

public class ExpressUser extends EhExpressUsers {
	
	private static final long serialVersionUID = 8209541294761146211L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}