package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>scriptId: scriptId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>scriptType: 脚本类型 {@link com.everhomes.rest.flow.FlowScriptType}</li>
 * </ul>
 */
public class ListFlowScriptConfigsCommand {

    @NotNull
    private Long scriptId;
    @NotNull
    private Long moduleId;
    @NotNull
    private String scriptType;

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
