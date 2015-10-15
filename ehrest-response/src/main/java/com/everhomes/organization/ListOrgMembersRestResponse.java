// @formatter:off
// generated at 2015-10-15 10:45:21
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.ListOrganizationMemberCommandResponse;

public class ListOrgMembersRestResponse extends RestResponseBase {

    private ListOrganizationMemberCommandResponse response;

    public ListOrgMembersRestResponse () {
    }

    public ListOrganizationMemberCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListOrganizationMemberCommandResponse response) {
        this.response = response;
    }
}
