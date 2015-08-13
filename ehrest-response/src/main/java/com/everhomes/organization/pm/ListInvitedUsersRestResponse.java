// @formatter:off
// generated at 2015-08-13 10:45:22
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropInvitedUserCommandResponse;

public class ListInvitedUsersRestResponse extends RestResponseBase {

    private ListPropInvitedUserCommandResponse response;

    public ListInvitedUsersRestResponse () {
    }

    public ListPropInvitedUserCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropInvitedUserCommandResponse response) {
        this.response = response;
    }
}
