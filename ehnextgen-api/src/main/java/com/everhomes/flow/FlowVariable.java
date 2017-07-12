package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowVariables;
import com.everhomes.util.StringHelper;

public class FlowVariable extends EhFlowVariables {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4329739897258226388L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
