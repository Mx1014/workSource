// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.GroupMemberDTO;

public class ListGroupWaitingAcceptancesRestResponse extends RestResponseBase {

    private List<GroupMemberDTO> response;

    public ListGroupWaitingAcceptancesRestResponse () {
    }

    public List<GroupMemberDTO> getResponse() {
        return response;
    }

    public void setResponse(List<GroupMemberDTO> response) {
        this.response = response;
    }
}
