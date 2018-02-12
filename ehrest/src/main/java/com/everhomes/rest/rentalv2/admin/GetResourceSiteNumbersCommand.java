package com.everhomes.rest.rentalv2.admin;

/**
 * Created by Administrator on 2018/2/12.
 */
public class GetResourceSiteNumbersCommand {

    private Long rentalSiteId;
    private String resourceType;

    public Long getRentalSiteId() {
        return rentalSiteId;
    }

    public void setRentalSiteId(Long rentalSiteId) {
        this.rentalSiteId = rentalSiteId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
