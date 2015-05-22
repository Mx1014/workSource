// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropInvitedUserCommandResponse;

public class ListPropFamilyWaitingMemberRestResponse extends RestResponseBase {

    private List<ListPropInvitedUserCommandResponse> response;

    public ListPropFamilyWaitingMemberRestResponse () {
    }

    public List<ListPropInvitedUserCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropInvitedUserCommandResponse> response) {
        this.response = response;
    }
}
