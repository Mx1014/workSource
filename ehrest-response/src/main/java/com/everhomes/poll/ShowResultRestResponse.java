// @formatter:off
// generated at 2015-09-08 16:00:43
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
