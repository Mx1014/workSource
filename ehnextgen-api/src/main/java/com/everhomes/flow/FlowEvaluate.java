package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowEvaluates;
import com.everhomes.util.StringHelper;

public class FlowEvaluate extends EhFlowEvaluates {
    /**
	 * 
	 */
	private static final long serialVersionUID = 533022113636399135L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
