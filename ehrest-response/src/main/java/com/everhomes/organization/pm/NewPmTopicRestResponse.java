// @formatter:off
// generated at 2015-07-09 01:51:43
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
