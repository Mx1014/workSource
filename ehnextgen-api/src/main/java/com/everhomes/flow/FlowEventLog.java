package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.util.StringHelper;

public class FlowEventLog extends EhFlowEventLogs {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6063163152705295466L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
