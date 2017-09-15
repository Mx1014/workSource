// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressHotlines;
import com.everhomes.util.StringHelper;

public class ExpressHotline extends EhExpressHotlines {
	
	private static final long serialVersionUID = -7728542792195153441L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}