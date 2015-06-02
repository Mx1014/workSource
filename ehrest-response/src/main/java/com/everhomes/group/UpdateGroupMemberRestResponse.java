// @formatter:off
// generated at 2015-06-01 21:15:04
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.GroupMemberDTO;

public class UpdateGroupMemberRestResponse extends RestResponseBase {

    private GroupMemberDTO response;

    public UpdateGroupMemberRestResponse () {
    }

    public GroupMemberDTO getResponse() {
        return response;
    }

    public void setResponse(GroupMemberDTO response) {
        this.response = response;
    }
}
