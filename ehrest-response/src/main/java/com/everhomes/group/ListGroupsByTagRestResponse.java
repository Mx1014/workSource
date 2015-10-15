// @formatter:off
// generated at 2015-10-15 10:18:58
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
