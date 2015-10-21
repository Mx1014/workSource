// @formatter:off
// generated at 2015-10-21 17:44:18
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
