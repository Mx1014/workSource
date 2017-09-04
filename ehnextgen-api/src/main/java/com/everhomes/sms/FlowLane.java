// @formatter:off
package com.everhomes.sms;

import com.everhomes.server.schema.tables.pojos.EhFlowLanes;
import com.everhomes.util.StringHelper;

public class FlowLane extends EhFlowLanes {
	
	private static final long serialVersionUID = -8217636393105889939L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}