// @formatter:off
// generated at 2015-07-09 23:44:43
package com.everhomes.realestate;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class PostRestResponse extends RestResponseBase {

    private PostDTO response;

    public PostRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
