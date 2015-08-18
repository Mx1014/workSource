// @formatter:off
// generated at 2015-08-18 15:16:38
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropAddressMappingCommandResponse;

public class GetPMAddressMappingRestResponse extends RestResponseBase {

    private ListPropAddressMappingCommandResponse response;

    public GetPMAddressMappingRestResponse () {
    }

    public ListPropAddressMappingCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropAddressMappingCommandResponse response) {
        this.response = response;
    }
}
