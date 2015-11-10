// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.GroupDTO;

public class UpdateRestResponse extends RestResponseBase {

    private GroupDTO response;

    public UpdateRestResponse () {
    }

    public GroupDTO getResponse() {
        return response;
    }

    public void setResponse(GroupDTO response) {
        this.response = response;
    }
}
