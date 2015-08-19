// @formatter:off
// generated at 2015-08-19 15:26:32
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
