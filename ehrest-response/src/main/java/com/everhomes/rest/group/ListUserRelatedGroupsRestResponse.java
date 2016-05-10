// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.group.GroupDTO;

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
