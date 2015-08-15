// @formatter:off
// generated at 2015-08-14 13:59:48
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PmBillsDTO;

public class FindFamilyNewestBillByFamilyIdRestResponse extends RestResponseBase {

    private PmBillsDTO response;

    public FindFamilyNewestBillByFamilyIdRestResponse () {
    }

    public PmBillsDTO getResponse() {
        return response;
    }

    public void setResponse(PmBillsDTO response) {
        this.response = response;
    }
}
