package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>id: 脚本id</li>
 *     <li>scriptType: 脚本类型 {@link com.everhomes.rest.flow.FlowScriptType}</li>
 *     <li>scriptVersion: 版本号</li>
 *     <li>configs: 脚本配置列表{@link com.everhomes.rest.flow.FlowScriptConfigInfo}</li>
 * </ul>
 */
public class FlowScriptInfo {

    @NotNull
    private Long id;
    @NotNull
    private String scriptType;
    @NotNull
    private Integer scriptVersion;

    private List<FlowScriptConfigInfo> configs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FlowScriptConfigInfo> getConfigs() {
        return configs;
    }

    public void setConfigs(List<FlowScriptConfigInfo> configs) {
        this.configs = configs;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public Integer getScriptVersion() {
        return scriptVersion;
    }

    public void setScriptVersion(Integer scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
