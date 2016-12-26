package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowTimeouts;
import com.everhomes.util.StringHelper;

public class FlowTimeout extends EhFlowTimeouts {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6746536219202264536L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
