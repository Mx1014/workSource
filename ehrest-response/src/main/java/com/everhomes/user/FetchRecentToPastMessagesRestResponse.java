// @formatter:off
// generated at 2015-08-19 15:26:32
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
