// @formatter:off
// generated at 2015-08-11 15:30:31
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
