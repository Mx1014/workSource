// @formatter:off
// generated at 2015-10-27 13:49:25
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class ListTopicCommentsRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public ListTopicCommentsRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
