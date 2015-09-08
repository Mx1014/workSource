// @formatter:off
// generated at 2015-09-08 14:51:07
package com.everhomes.organization.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.ListOrganizationsCommandResponse;

public class OrgRestResponse extends RestResponseBase {

    private ListOrganizationsCommandResponse response;

    public OrgRestResponse () {
    }

    public ListOrganizationsCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListOrganizationsCommandResponse response) {
        this.response = response;
    }
}
