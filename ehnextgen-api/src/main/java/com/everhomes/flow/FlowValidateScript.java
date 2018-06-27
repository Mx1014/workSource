package com.everhomes.flow;

import com.everhomes.util.StringHelper;

public class FlowValidateScript extends FlowRuntimeScript {

    public FlowValidateScript(Long scriptMainId, Integer scriptVersion) {
        super(scriptMainId, scriptVersion);
    }

    public FlowValidateScript(Long scriptMainId, Integer scriptVersion, String script) {
        super(scriptMainId, scriptVersion, script);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
