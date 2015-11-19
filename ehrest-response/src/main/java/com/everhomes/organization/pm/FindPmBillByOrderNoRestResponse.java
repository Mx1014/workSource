// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PmBillForOrderNoDTO;

public class FindPmBillByOrderNoRestResponse extends RestResponseBase {

    private PmBillForOrderNoDTO response;

    public FindPmBillByOrderNoRestResponse () {
    }

    public PmBillForOrderNoDTO getResponse() {
        return response;
    }

    public void setResponse(PmBillForOrderNoDTO response) {
        this.response = response;
    }
}
