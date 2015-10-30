// @formatter:off
// generated at 2015-10-30 14:21:35
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
