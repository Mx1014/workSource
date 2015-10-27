// @formatter:off
// generated at 2015-10-27 15:48:23
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.ListGroupCommandResponse;

public class SearchRestResponse extends RestResponseBase {

    private ListGroupCommandResponse response;

    public SearchRestResponse () {
    }

    public ListGroupCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListGroupCommandResponse response) {
        this.response = response;
    }
}
