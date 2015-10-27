// @formatter:off
// generated at 2015-10-27 13:49:25
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.OrganizationDTO;

public class GetOrganizationDetailsRestResponse extends RestResponseBase {

    private OrganizationDTO response;

    public GetOrganizationDetailsRestResponse () {
    }

    public OrganizationDTO getResponse() {
        return response;
    }

    public void setResponse(OrganizationDTO response) {
        this.response = response;
    }
}
