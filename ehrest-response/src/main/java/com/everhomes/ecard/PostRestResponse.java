// @formatter:off
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:17
>>>>>>> update ehrest-response 2015/10/21
=======
// generated at 2015-10-26 15:50:45
>>>>>>> modify recommend biz and add deleteLaunchPadItem method
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
