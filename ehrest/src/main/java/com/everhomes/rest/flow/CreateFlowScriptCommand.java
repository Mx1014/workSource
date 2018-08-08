package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>name: 名称</li>
 *     <li>description: 描述</li>
 *     <li>script: 脚本内容</li>
 *     <li>scriptType: 脚本类型{@link com.everhomes.rest.flow.FlowScriptType}</li>
 * </ul>
 */
public class CreateFlowScriptCommand {

    @NotNull
    private Long flowId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private String script;
    @NotNull
    private String scriptType;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
