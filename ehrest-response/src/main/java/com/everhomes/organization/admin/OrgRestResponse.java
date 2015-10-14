// @formatter:off
// generated at 2015-10-14 12:36:35
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
