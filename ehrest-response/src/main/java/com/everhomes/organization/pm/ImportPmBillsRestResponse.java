// @formatter:off
// generated at 2015-07-16 11:20:45
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPmBillsByConditionsCommandResponse;

public class ImportPmBillsRestResponse extends RestResponseBase {

    private ListPmBillsByConditionsCommandResponse response;

    public ImportPmBillsRestResponse () {
    }

    public ListPmBillsByConditionsCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPmBillsByConditionsCommandResponse response) {
        this.response = response;
    }
}
