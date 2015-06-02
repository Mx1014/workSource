// @formatter:off
// generated at 2015-06-01 21:15:04
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
