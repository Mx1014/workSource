// @formatter:off
// generated at 2015-08-10 20:34:45
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
