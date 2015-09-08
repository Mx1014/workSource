// @formatter:off
// generated at 2015-09-08 19:31:04
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropOwnerCommandResponse;

public class ListPMPropertyOwnerInfoRestResponse extends RestResponseBase {

    private ListPropOwnerCommandResponse response;

    public ListPMPropertyOwnerInfoRestResponse () {
    }

    public ListPropOwnerCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropOwnerCommandResponse response) {
        this.response = response;
    }
}
