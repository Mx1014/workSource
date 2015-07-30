// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.admin.organization;

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
