// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropOwnerCommandResponse;

public class GetPMPropertyOwnerInfoRestResponse extends RestResponseBase {

    private List<ListPropOwnerCommandResponse> response;

    public GetPMPropertyOwnerInfoRestResponse () {
    }

    public List<ListPropOwnerCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropOwnerCommandResponse> response) {
        this.response = response;
    }
}
