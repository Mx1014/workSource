// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropMemberCommandResponse;

public class ListPMGroupMembersRestResponse extends RestResponseBase {

    private List<ListPropMemberCommandResponse> response;

    public ListPMGroupMembersRestResponse () {
    }

    public List<ListPropMemberCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropMemberCommandResponse> response) {
        this.response = response;
    }
}
