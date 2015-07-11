// @formatter:off
// generated at 2015-07-11 16:05:49
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class NewCommentRestResponse extends RestResponseBase {

    private PostDTO response;

    public NewCommentRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
