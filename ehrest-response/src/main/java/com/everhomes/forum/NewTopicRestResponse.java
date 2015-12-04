// @formatter:off
// generated at 2015-12-04 14:52:02
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
