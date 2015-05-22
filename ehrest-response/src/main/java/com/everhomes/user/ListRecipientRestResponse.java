// @formatter:off
// generated at 2015-05-21 22:00:49
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
