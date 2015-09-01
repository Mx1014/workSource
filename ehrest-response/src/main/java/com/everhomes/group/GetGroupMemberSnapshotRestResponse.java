// @formatter:off
// generated at 2015-09-01 15:16:07
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.group.GroupMemberSnapshotDTO;

public class GetGroupMemberSnapshotRestResponse extends RestResponseBase {

    private GroupMemberSnapshotDTO response;

    public GetGroupMemberSnapshotRestResponse () {
    }

    public GroupMemberSnapshotDTO getResponse() {
        return response;
    }

    public void setResponse(GroupMemberSnapshotDTO response) {
        this.response = response;
    }
}
