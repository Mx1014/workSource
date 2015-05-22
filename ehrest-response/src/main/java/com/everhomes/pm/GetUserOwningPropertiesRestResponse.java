// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pm.ListPropMemberCommandResponse;

public class GetUserOwningPropertiesRestResponse extends RestResponseBase {

    private ListPropMemberCommandResponse response;

    public GetUserOwningPropertiesRestResponse () {
    }

    public ListPropMemberCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropMemberCommandResponse response) {
        this.response = response;
    }
}
