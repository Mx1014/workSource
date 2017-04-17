// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressUsers;
import com.everhomes.util.StringHelper;

public class ExpressUser extends EhExpressUsers {
	
	private static final long serialVersionUID = -1964597274307251212L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}