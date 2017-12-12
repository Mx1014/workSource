// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressCompanyBusinesses;
import com.everhomes.util.StringHelper;

public class ExpressCompanyBusiness extends EhExpressCompanyBusinesses {
	
	private static final long serialVersionUID = -1015038360987242251L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}