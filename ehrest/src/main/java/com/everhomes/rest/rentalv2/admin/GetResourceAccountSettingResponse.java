package com.everhomes.rest.rentalv2.admin;


import java.util.List;

public class GetResourceAccountSettingResponse {

    private Long nextPageAnchor;
    private List<ResourceAccountDTO> resourceAccounts;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ResourceAccountDTO> getResourceAccounts() {
        return resourceAccounts;
    }

    public void setResourceAccounts(List<ResourceAccountDTO> resourceAccounts) {
        this.resourceAccounts = resourceAccounts;
    }
}
