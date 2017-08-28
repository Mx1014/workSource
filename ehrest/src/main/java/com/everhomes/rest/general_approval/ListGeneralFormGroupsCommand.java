package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

public class ListGeneralFormGroupsCommand {

    private Long organizationId;

    private Long formOriginId;

    public ListGeneralFormGroupsCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
