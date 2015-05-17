// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.pm.ListPropBillCommandResponse;

public class ListPropertyBillByTimeRangeRestResponse extends RestResponseBase {

    private List<ListPropBillCommandResponse> response;

    public ListPropertyBillByTimeRangeRestResponse () {
    }

    public List<ListPropBillCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListPropBillCommandResponse> response) {
        this.response = response;
    }
}
