package com.everhomes.rest.techpark.expansion;

/**
 * <ul>
 * <li>organizationId：公司id</li>
 * </ul>
 */
public class CheckIsLeaseIssuerCommand {
    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
