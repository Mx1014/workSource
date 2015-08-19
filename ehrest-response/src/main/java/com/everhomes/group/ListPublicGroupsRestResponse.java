// @formatter:off
// generated at 2015-08-19 15:26:32
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
