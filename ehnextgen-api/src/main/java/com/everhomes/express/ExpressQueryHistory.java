// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressQueryHistories;
import com.everhomes.util.StringHelper;

public class ExpressQueryHistory extends EhExpressQueryHistories {
	
	private static final long serialVersionUID = 685385763942377660L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}