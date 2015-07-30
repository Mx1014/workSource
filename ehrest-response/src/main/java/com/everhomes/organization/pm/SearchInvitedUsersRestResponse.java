// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropInvitedUserCommandResponse;

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
