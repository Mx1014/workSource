// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class GetTopicRestResponse extends RestResponseBase {

    private PostDTO response;

    public GetTopicRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
