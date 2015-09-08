// @formatter:off
// generated at 2015-09-08 16:00:43
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PmBillsDTO;

public class FindNewestBillByAddressIdRestResponse extends RestResponseBase {

    private PmBillsDTO response;

    public FindNewestBillByAddressIdRestResponse () {
    }

    public PmBillsDTO getResponse() {
        return response;
    }

    public void setResponse(PmBillsDTO response) {
        this.response = response;
    }
}
