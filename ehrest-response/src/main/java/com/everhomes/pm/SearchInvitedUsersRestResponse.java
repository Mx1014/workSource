// @formatter:off
// generated at 2015-06-01 21:15:04
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pm.ListPropInvitedUserCommandResponse;

public class SearchInvitedUsersRestResponse extends RestResponseBase {

    private ListPropInvitedUserCommandResponse response;

    public SearchInvitedUsersRestResponse () {
    }

    public ListPropInvitedUserCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropInvitedUserCommandResponse response) {
        this.response = response;
    }
}
