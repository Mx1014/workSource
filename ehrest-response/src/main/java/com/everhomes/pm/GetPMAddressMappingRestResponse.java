// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropAddressMappingCommandResponse;

public class GetPMAddressMappingRestResponse extends RestResponseBase {

    private List<ListPropAddressMappingCommandResponse> response;

    public GetPMAddressMappingRestResponse () {
    }

    public List<ListPropAddressMappingCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropAddressMappingCommandResponse> response) {
        this.response = response;
    }
}
