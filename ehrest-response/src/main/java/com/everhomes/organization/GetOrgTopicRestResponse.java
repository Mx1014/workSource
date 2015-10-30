// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.PostDTO;

public class GetOrgTopicRestResponse extends RestResponseBase {

    private PostDTO response;

    public GetOrgTopicRestResponse () {
    }

    public PostDTO getResponse() {
        return response;
    }

    public void setResponse(PostDTO response) {
        this.response = response;
    }
}
