// @formatter:off
// generated at 2015-11-10 11:23:24
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
