// @formatter:off
// generated at 2015-11-03 16:20:53
// generated at 2015-10-21 17:44:18
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
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
