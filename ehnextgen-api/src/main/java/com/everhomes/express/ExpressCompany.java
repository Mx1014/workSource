// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressCompanies;
import com.everhomes.util.StringHelper;

public class ExpressCompany extends EhExpressCompanies {
	
	private static final long serialVersionUID = 3534050949005318526L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}