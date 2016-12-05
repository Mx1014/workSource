package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowForms;
import com.everhomes.util.StringHelper;

public class FlowForm extends EhFlowForms {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3281187141007055988L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
