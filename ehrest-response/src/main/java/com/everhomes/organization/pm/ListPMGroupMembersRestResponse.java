// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropMemberCommandResponse;

public class ListPMGroupMembersRestResponse extends RestResponseBase {

    private ListPropMemberCommandResponse response;

    public ListPMGroupMembersRestResponse () {
    }

    public ListPropMemberCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropMemberCommandResponse response) {
        this.response = response;
    }
}
