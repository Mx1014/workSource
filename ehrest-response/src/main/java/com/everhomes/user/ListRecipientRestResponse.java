// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.InvitationCommandResponse;

public class ListRecipientRestResponse extends RestResponseBase {

    private InvitationCommandResponse response;

    public ListRecipientRestResponse () {
    }

    public InvitationCommandResponse getResponse() {
        return response;
    }

    public void setResponse(InvitationCommandResponse response) {
        this.response = response;
    }
}
