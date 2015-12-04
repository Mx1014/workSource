// @formatter:off
// generated at 2015-12-04 14:52:02
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
