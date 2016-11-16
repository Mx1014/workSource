package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowActions;
import com.everhomes.util.StringHelper;

public class FlowAction extends EhFlowActions {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3805976892366556884L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
