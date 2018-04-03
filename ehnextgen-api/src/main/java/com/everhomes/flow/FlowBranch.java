// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowBranches;
import com.everhomes.util.StringHelper;

public class FlowBranch extends EhFlowBranches {
	
	private static final long serialVersionUID = 4952793721404563405L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}