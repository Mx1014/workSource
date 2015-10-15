// @formatter:off
// generated at 2015-10-15 09:49:20
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.ListNearbyGroupCommandResponse;

public class ListNearbyGroupsRestResponse extends RestResponseBase {

    private ListNearbyGroupCommandResponse response;

    public ListNearbyGroupsRestResponse () {
    }

    public ListNearbyGroupCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListNearbyGroupCommandResponse response) {
        this.response = response;
    }
}
