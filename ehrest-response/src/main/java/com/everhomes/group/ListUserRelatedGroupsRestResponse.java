// @formatter:off
// generated at 2015-10-30 14:21:35
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
