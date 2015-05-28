// @formatter:off
// generated at 2015-05-27 21:29:38
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
