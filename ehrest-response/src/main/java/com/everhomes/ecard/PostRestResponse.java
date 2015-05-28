// @formatter:off
// generated at 2015-05-27 21:29:38
package com.everhomes.ecard;

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
