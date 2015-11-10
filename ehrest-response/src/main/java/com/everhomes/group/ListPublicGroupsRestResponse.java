// @formatter:off
// generated at 2015-11-10 14:30:36
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.GroupDTO;

public class ListPublicGroupsRestResponse extends RestResponseBase {

    private List<GroupDTO> response;

    public ListPublicGroupsRestResponse () {
    }

    public List<GroupDTO> getResponse() {
        return response;
    }

    public void setResponse(List<GroupDTO> response) {
        this.response = response;
    }
}
