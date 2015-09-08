// @formatter:off
// generated at 2015-09-08 19:31:04
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropMemberCommandResponse;

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
