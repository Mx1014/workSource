// @formatter:off
// generated at 2015-08-13 10:45:22
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
