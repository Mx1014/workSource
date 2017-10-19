// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowConditions;
import com.everhomes.util.StringHelper;

public class FlowCondition extends EhFlowConditions {
	
	private static final long serialVersionUID = 4449245509555398245L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}