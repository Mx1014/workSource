// @formatter:off
// generated at 2015-11-19 19:54:45
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
