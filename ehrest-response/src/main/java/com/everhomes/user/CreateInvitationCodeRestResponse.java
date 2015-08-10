// @formatter:off
// generated at 2015-08-10 11:20:28
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
