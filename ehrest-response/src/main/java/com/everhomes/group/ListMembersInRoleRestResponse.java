// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.ListMemberCommandResponse;

public class ListMembersInRoleRestResponse extends RestResponseBase {

    private List<ListMemberCommandResponse> response;

    public ListMembersInRoleRestResponse () {
    }

    public List<ListMemberCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListMemberCommandResponse> response) {
        this.response = response;
    }
}
