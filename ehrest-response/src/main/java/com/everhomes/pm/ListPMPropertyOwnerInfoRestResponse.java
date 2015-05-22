// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropOwnerCommandResponse;

public class ListPMPropertyOwnerInfoRestResponse extends RestResponseBase {

    private List<ListPropOwnerCommandResponse> response;

    public ListPMPropertyOwnerInfoRestResponse () {
    }

    public List<ListPropOwnerCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropOwnerCommandResponse> response) {
        this.response = response;
    }
}
