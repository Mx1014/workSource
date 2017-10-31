// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowServiceTypes;
import com.everhomes.util.StringHelper;

public class FlowServiceType extends EhFlowServiceTypes {
	
	private static final long serialVersionUID = 5044849263582894769L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}