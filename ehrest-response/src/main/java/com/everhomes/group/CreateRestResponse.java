// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.GroupDTO;

public class CreateRestResponse extends RestResponseBase {

    private GroupDTO response;

    public CreateRestResponse () {
    }

    public GroupDTO getResponse() {
        return response;
    }

    public void setResponse(GroupDTO response) {
        this.response = response;
    }
}
