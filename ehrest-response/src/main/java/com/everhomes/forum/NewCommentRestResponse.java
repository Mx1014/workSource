// @formatter:off
// generated at 2015-08-04 16:41:43
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
