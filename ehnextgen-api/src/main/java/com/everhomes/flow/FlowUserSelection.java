package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowUserSelections;
import com.everhomes.util.StringHelper;

public class FlowUserSelection extends EhFlowUserSelections {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4326912846677972449L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
