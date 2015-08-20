// @formatter:off
// generated at 2015-08-20 18:05:54
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class NewOrgCommentRestResponse extends RestResponseBase {

    private PostDTO response;

    public NewOrgCommentRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
