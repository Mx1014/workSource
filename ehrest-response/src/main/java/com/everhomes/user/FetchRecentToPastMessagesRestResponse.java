// @formatter:off
// generated at 2015-10-27 15:08:14
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.FetchMessageCommandResponse;

public class FetchRecentToPastMessagesRestResponse extends RestResponseBase {

    private FetchMessageCommandResponse response;

    public FetchRecentToPastMessagesRestResponse () {
    }

    public FetchMessageCommandResponse getResponse() {
        return response;
    }

    public void setResponse(FetchMessageCommandResponse response) {
        this.response = response;
    }
}
