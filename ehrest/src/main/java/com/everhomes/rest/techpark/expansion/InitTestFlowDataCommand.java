package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/12/20.
 */
public class InitTestFlowDataCommand {

    private Integer namespaceId;
    private Long organizationId;
    private Long moduleId;
    private String identifierToken;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
