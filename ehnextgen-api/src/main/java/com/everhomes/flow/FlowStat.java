package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowStats;
import com.everhomes.util.StringHelper;

public class FlowStat extends EhFlowStats {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2645350012377456241L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
