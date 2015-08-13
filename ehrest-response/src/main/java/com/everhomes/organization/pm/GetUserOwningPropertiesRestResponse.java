// @formatter:off
// generated at 2015-08-13 10:45:22
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
