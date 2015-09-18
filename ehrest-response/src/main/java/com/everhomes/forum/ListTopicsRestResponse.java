// @formatter:off
// generated at 2015-09-18 18:44:17
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class ListTopicsRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public ListTopicsRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
