// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class GetPmTopicRestResponse extends RestResponseBase {

    private PostDTO response;

    public GetPmTopicRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
