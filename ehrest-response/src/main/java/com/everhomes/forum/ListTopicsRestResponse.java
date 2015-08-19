// @formatter:off
// generated at 2015-08-19 15:26:32
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
