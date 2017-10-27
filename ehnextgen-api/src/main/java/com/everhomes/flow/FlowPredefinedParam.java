// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowPredefinedParams;
import com.everhomes.util.StringHelper;

public class FlowPredefinedParam extends EhFlowPredefinedParams {
	
	private static final long serialVersionUID = 5834389039526221514L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}