// @formatter:off
// generated at 2015-07-16 11:20:45
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPmBillsByConditionsCommandResponse;

public class ListPmBillsByConditionsRestResponse extends RestResponseBase {

    private ListPmBillsByConditionsCommandResponse response;

    public ListPmBillsByConditionsRestResponse () {
    }

    public ListPmBillsByConditionsCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPmBillsByConditionsCommandResponse response) {
        this.response = response;
    }
}
