package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowEvaluateItems;
import com.everhomes.util.StringHelper;

public class FlowEvaluateItem extends EhFlowEvaluateItems {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5027682663794035892L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
