// @formatter:off
// generated at 2015-11-10 11:13:10
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
