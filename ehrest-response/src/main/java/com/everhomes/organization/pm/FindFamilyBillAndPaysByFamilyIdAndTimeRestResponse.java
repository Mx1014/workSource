// @formatter:off
// generated at 2015-10-14 12:36:35
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
