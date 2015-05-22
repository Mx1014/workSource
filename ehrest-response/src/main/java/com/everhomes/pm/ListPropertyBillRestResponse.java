// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropBillCommandResponse;

public class ListPropertyBillRestResponse extends RestResponseBase {

    private List<ListPropBillCommandResponse> response;

    public ListPropertyBillRestResponse () {
    }

    public List<ListPropBillCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropBillCommandResponse> response) {
        this.response = response;
    }
}
