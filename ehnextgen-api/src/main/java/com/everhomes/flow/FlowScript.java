package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowScripts;
import com.everhomes.util.StringHelper;

public class FlowScript extends EhFlowScripts {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5214698892074997634L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public void incrementVersion() {
	    setScriptVersion(getScriptVersion() + 1);
    }

    public Long getTopId() {
        return this.getScriptMainId() != 0L ? this.getScriptMainId() : this.getId();
    }

    public String gogsPath() {
        return this.getName() + ".js";
    }
}
