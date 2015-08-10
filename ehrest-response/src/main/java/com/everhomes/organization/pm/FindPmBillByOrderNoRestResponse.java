// @formatter:off
// generated at 2015-08-10 11:20:28
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
