// @formatter:off
// generated at 2015-11-20 09:40:32
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
