// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class CreateTopicRestResponse extends RestResponseBase {

    private PostDTO response;

    public CreateTopicRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
