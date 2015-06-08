// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.GroupDTO;

public class ListUserRelatedGroupsRestResponse extends RestResponseBase {

    private List<GroupDTO> response;

    public ListUserRelatedGroupsRestResponse () {
    }

    public List<GroupDTO> getResponse() {
        return response;
    }

    public void setResponse(List<GroupDTO> response) {
        this.response = response;
    }
}
