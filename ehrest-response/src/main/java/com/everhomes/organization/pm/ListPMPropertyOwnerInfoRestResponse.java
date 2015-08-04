// @formatter:off
// generated at 2015-08-04 16:41:43
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
