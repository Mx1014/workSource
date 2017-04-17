// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressQueryHistories;
import com.everhomes.util.StringHelper;

public class ExpressQueryHistory extends EhExpressQueryHistories {
	
	private static final long serialVersionUID = -7502444510953186132L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}