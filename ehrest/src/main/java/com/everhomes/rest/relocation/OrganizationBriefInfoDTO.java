package com.everhomes.rest.relocation;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/11/21.
 */
public class OrganizationBriefInfoDTO {
    private Long organizationId;
    private String organizationName;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
