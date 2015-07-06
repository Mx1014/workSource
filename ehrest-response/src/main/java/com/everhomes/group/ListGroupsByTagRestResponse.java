// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.ListGroupCommandResponse;

public class ListGroupsByTagRestResponse extends RestResponseBase {

    private ListGroupCommandResponse response;

    public ListGroupsByTagRestResponse () {
    }

    public ListGroupCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListGroupCommandResponse response) {
        this.response = response;
    }
}
