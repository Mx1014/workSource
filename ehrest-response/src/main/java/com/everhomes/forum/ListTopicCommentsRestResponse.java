// @formatter:off
// generated at 2015-08-14 13:59:48
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
