// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.quality;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.quality.GroupUserDTO;

public class GetGroupMembersRestResponse extends RestResponseBase {

    private List<GroupUserDTO> response;

    public GetGroupMembersRestResponse () {
    }

    public List<GroupUserDTO> getResponse() {
        return response;
    }

    public void setResponse(List<GroupUserDTO> response) {
        this.response = response;
    }
}
