// @formatter:off
// generated at 2015-11-03 16:20:54
// generated at 2015-10-21 17:44:18
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
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
