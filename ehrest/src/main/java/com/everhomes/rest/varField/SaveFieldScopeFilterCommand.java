package com.everhomes.rest.varField;

import java.util.List;

public class SaveFieldScopeFilterCommand {

    private List<Long> fieldId;

    private Integer namespaceId;

    private Long communityId;

    private String moduleName;

    private String groupPath;

    public String getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<Long> getFieldId() {
        return fieldId;
    }

    public void setFieldId(List<Long> fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
