// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowConditionExpressions;
import com.everhomes.util.StringHelper;

public class FlowConditionExpression extends EhFlowConditionExpressions {
	
	private static final long serialVersionUID = -1794671752490309326L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}