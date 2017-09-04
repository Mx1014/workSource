// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowWays;
import com.everhomes.util.StringHelper;

public class FlowWay extends EhFlowWays {
	
	private static final long serialVersionUID = -5791406669477529764L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}