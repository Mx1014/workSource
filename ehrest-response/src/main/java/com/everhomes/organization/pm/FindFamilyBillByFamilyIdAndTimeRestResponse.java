// @formatter:off
// generated at 2015-08-14 10:20:50
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PmBillsDTO;

public class FindFamilyBillByFamilyIdAndTimeRestResponse extends RestResponseBase {

    private PmBillsDTO response;

    public FindFamilyBillByFamilyIdAndTimeRestResponse () {
    }

    public PmBillsDTO getResponse() {
        return response;
    }

    public void setResponse(PmBillsDTO response) {
        this.response = response;
    }
}
