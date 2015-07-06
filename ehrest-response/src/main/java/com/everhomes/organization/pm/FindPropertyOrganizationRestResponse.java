// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.OrganizationDTO;

public class FindPropertyOrganizationRestResponse extends RestResponseBase {

    private OrganizationDTO response;

    public FindPropertyOrganizationRestResponse () {
    }

    public OrganizationDTO getResponse() {
        return response;
    }

    public void setResponse(OrganizationDTO response) {
        this.response = response;
    }
}
