// @formatter:off
// generated at 2015-10-21 17:44:18
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
