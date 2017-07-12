package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowButtons;
import com.everhomes.util.StringHelper;

public class FlowButton extends EhFlowButtons {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8193253311166064674L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
