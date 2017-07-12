package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowNodes;
import com.everhomes.util.StringHelper;

public class FlowNode extends EhFlowNodes {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7408368827319673502L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
