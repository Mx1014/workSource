// @formatter:off
// generated at 2015-07-11 14:26:36
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
