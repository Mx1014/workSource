// @formatter:off
// generated at 2015-08-10 11:20:28
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
