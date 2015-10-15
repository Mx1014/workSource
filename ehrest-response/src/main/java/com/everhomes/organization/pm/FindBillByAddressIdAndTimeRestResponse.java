// @formatter:off
// generated at 2015-10-15 09:49:20
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PmBillsDTO;

public class FindBillByAddressIdAndTimeRestResponse extends RestResponseBase {

    private PmBillsDTO response;

    public FindBillByAddressIdAndTimeRestResponse () {
    }

    public PmBillsDTO getResponse() {
        return response;
    }

    public void setResponse(PmBillsDTO response) {
        this.response = response;
    }
}
