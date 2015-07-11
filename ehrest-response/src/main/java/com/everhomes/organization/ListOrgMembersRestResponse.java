// @formatter:off
// generated at 2015-07-11 16:05:49
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
