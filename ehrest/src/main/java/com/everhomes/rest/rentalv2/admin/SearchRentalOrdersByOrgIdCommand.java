package com.everhomes.rest.rentalv2.admin;


public class SearchRentalOrdersByOrgIdCommand {

    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
