// @formatter:off
// generated at 2015-07-11 14:26:36
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.GroupDTO;

public class GetRestResponse extends RestResponseBase {

    private GroupDTO response;

    public GetRestResponse () {
    }

    public GroupDTO getResponse() {
        return response;
    }

    public void setResponse(GroupDTO response) {
        this.response = response;
    }
}
