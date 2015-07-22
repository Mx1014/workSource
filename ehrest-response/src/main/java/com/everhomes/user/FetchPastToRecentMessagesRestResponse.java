// @formatter:off
// generated at 2015-07-22 15:04:22
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.FetchMessageCommandResponse;

public class FetchPastToRecentMessagesRestResponse extends RestResponseBase {

    private FetchMessageCommandResponse response;

    public FetchPastToRecentMessagesRestResponse () {
    }

    public FetchMessageCommandResponse getResponse() {
        return response;
    }

    public void setResponse(FetchMessageCommandResponse response) {
        this.response = response;
    }
}
