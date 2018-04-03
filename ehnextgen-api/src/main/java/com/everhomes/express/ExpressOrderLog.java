// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressOrderLogs;
import com.everhomes.util.StringHelper;

public class ExpressOrderLog extends EhExpressOrderLogs {
	
	private static final long serialVersionUID = 2435587154869147481L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}