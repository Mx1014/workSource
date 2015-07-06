// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserInvitationsDTO;

public class CreateInvitationCodeRestResponse extends RestResponseBase {

    private UserInvitationsDTO response;

    public CreateInvitationCodeRestResponse () {
    }

    public UserInvitationsDTO getResponse() {
        return response;
    }

    public void setResponse(UserInvitationsDTO response) {
        this.response = response;
    }
}
