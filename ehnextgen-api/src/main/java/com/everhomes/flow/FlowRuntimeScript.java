package com.everhomes.flow;

import com.everhomes.util.StringHelper;

public class FlowRuntimeScript {

    private Long scriptMainId;
    private Integer scriptVersion;
    private String script;

    public FlowRuntimeScript(Long scriptMainId, Integer scriptVersion) {
        this.scriptMainId = scriptMainId;
        this.scriptVersion = scriptVersion;
    }

    public FlowRuntimeScript(Long scriptMainId, Integer scriptVersion, String script) {
        this.scriptMainId = scriptMainId;
        this.scriptVersion = scriptVersion;
        this.script = script;
    }

    public Long getScriptMainId() {
        return scriptMainId;
    }

    public void setScriptMainId(Long scriptMainId) {
        this.scriptMainId = scriptMainId;
    }

    public Integer getScriptVersion() {
        return scriptVersion;
    }

    public void setScriptVersion(Integer scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
