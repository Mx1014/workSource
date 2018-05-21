package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowNodeScriptCommand {

	private FlowActionInfo enterScript;

    public FlowActionInfo getEnterScript() {
        return enterScript;
    }

    public void setEnterScript(FlowActionInfo enterScript) {
        this.enterScript = enterScript;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
