package com.everhomes.rest.officecubicle;

import javax.validation.constraints.NotNull;

public class GetCurrentProjectOnlyFlagCommand {
    @NotNull
    private Integer namespaceId;
    private Long orgId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
