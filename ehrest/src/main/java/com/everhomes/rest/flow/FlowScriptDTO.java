package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleType: moduleType</li>
 *     <li>moduleId: moduleId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>scriptCategory: 脚本分类{@link com.everhomes.rest.flow.FlowScriptCategory}</li>
 *     <li>scriptType: 脚本类型,目前脚本对应于javascript,函数对对应于java{@link com.everhomes.rest.flow.FlowScriptType}</li>
 *     <li>scriptMainId: scriptMainId</li>
 *     <li>scriptVersion: scriptVersion</li>
 *     <li>name: 名称</li>
 *     <li>description: 描述</li>
 *     <li>script: 脚本内容</li>
 *     <li>status: status</li>
 *     <li>configs: configs {@link com.everhomes.rest.flow.FlowScriptConfigDTO}</li>
 *     <li>lastCommit: lastCommit</li>
 * </ul>
 */
public class FlowScriptDTO {

    private Long id;
    private Integer namespaceId;
    private String moduleType;
    private Long moduleId;
    private String ownerType;
    private Long ownerId;
    private String scriptCategory;
    private String scriptType;
    private Long scriptMainId;
    private Integer scriptVersion;
    private String name;
    private String description;
    private String script;
    private Byte status;
    private String lastCommit;

    private List<FlowScriptConfigDTO> configs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FlowScriptConfigDTO> getConfigs() {
        return configs;
    }

    public void setConfigs(List<FlowScriptConfigDTO> configs) {
        this.configs = configs;
    }

    public String getScriptCategory() {
        return scriptCategory;
    }

    public void setScriptCategory(String scriptCategory) {
        this.scriptCategory = scriptCategory;
    }

    public String getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(String lastCommit) {
        this.lastCommit = lastCommit;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
