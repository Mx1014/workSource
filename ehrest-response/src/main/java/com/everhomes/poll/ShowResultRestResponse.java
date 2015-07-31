// @formatter:off
// generated at 2015-07-31 09:39:28
package com.everhomes.poll;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.poll.PollShowResultResponse;

public class ShowResultRestResponse extends RestResponseBase {

    private PollShowResultResponse response;

    public ShowResultRestResponse () {
    }

    public PollShowResultResponse getResponse() {
        return response;
    }

    public void setResponse(PollShowResultResponse response) {
        this.response = response;
    }
}
