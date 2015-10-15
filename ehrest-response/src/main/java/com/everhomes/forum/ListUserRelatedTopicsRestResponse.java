// @formatter:off
// generated at 2015-10-15 10:18:58
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class ListUserRelatedTopicsRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public ListUserRelatedTopicsRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
