// @formatter:off
// generated at 2015-06-01 21:15:04
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pm.ListPropInvitedUserCommandResponse;

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
