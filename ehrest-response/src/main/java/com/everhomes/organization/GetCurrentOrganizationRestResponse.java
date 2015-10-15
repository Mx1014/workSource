// @formatter:off
// generated at 2015-10-15 10:18:58
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.OrganizationDTO;

public class GetCurrentOrganizationRestResponse extends RestResponseBase {

    private OrganizationDTO response;

    public GetCurrentOrganizationRestResponse () {
    }

    public OrganizationDTO getResponse() {
        return response;
    }

    public void setResponse(OrganizationDTO response) {
        this.response = response;
    }
}
