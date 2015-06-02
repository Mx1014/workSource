// @formatter:off
// generated at 2015-06-01 21:15:04
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pm.ListPropAddressMappingCommandResponse;

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
