// @formatter:off
// generated at 2015-10-26 15:50:45
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class NewOrgTopicRestResponse extends RestResponseBase {

    private PostDTO response;

    public NewOrgTopicRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
