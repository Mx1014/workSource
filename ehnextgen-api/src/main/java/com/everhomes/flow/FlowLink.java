// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowLinks;
import com.everhomes.util.StringHelper;

public class FlowLink extends EhFlowLinks {
	
	private static final long serialVersionUID = 6326001040648695502L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}