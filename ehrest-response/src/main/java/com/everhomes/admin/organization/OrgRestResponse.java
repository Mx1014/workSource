// @formatter:off
// generated at 2015-07-22 15:04:21
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
