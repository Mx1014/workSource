// @formatter:off
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2015-11-03 16:20:54
=======
// generated at 2015-10-21 17:44:18
>>>>>>> update ehrest-response 2015/10/21
=======
// generated at 2015-10-26 15:50:45
>>>>>>> modify recommend biz and add deleteLaunchPadItem method
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
