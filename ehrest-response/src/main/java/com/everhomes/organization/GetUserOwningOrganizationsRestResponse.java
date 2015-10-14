// @formatter:off
// generated at 2015-10-14 12:36:35
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
