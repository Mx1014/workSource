// @formatter:off
// generated at 2015-11-10 14:10:37
package com.everhomes.poll;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.poll.PollDTO;

public class VoteRestResponse extends RestResponseBase {

    private PollDTO response;

    public VoteRestResponse () {
    }

    public PollDTO getResponse() {
        return response;
    }

    public void setResponse(PollDTO response) {
        this.response = response;
    }
}
