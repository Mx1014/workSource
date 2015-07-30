// @formatter:off
// generated at 2015-07-30 19:26:52
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
