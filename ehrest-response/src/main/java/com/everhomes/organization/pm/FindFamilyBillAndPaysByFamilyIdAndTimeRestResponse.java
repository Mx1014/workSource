// @formatter:off
// generated at 2015-08-06 19:18:04
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PmBillsDTO;

public class FindFamilyBillAndPaysByFamilyIdAndTimeRestResponse extends RestResponseBase {

    private PmBillsDTO response;

    public FindFamilyBillAndPaysByFamilyIdAndTimeRestResponse () {
    }

    public PmBillsDTO getResponse() {
        return response;
    }

    public void setResponse(PmBillsDTO response) {
        this.response = response;
    }
}
