// @formatter:off
// generated at 2015-07-22 15:04:21
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
