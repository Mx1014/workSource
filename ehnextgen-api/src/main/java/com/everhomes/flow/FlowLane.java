// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowLanes;
import com.everhomes.util.StringHelper;

public class FlowLane extends EhFlowLanes {
	
	private static final long serialVersionUID = 2202498672164770383L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}