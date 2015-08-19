// @formatter:off
// generated at 2015-08-19 15:26:32
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class NewTopicRestResponse extends RestResponseBase {

    private PostDTO response;

    public NewTopicRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
