// @formatter:off
// generated at 2015-08-14 10:20:50
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class NewPmTopicRestResponse extends RestResponseBase {

    private PostDTO response;

    public NewPmTopicRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
