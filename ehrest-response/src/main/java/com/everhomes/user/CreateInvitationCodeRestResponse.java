// @formatter:off
// generated at 2015-07-22 15:04:22
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
