// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.ListOrganizationMemberCommandResponse;

public class GetUserOwningOrganizationsRestResponse extends RestResponseBase {

    private ListOrganizationMemberCommandResponse response;

    public GetUserOwningOrganizationsRestResponse () {
    }

    public ListOrganizationMemberCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListOrganizationMemberCommandResponse response) {
        this.response = response;
    }
}
