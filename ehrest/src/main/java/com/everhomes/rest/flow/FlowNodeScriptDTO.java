package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowNodeScriptDTO {

	private FlowActionDTO enterScript;

    public FlowActionDTO getEnterScript() {
        return enterScript;
    }

    public void setEnterScript(FlowActionDTO enterScript) {
        this.enterScript = enterScript;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
