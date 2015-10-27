// @formatter:off
// generated at 2015-11-03 16:20:54
// generated at 2015-10-21 17:44:18
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
// generated at 2015-10-27 15:48:23
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
