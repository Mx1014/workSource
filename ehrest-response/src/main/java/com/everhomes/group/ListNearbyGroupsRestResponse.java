// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.ListGroupCommandResponse;

public class ListNearbyGroupsRestResponse extends RestResponseBase {

    private ListGroupCommandResponse response;

    public ListNearbyGroupsRestResponse () {
    }

    public ListGroupCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListGroupCommandResponse response) {
        this.response = response;
    }
}
