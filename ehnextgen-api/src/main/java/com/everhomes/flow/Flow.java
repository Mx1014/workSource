package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlows;
import com.everhomes.util.StringHelper;

public class Flow extends EhFlows {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7283749249293245544L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
