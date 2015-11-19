// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.family.FamilyDTO;

public class FindFamilyByAddressIdRestResponse extends RestResponseBase {

    private FamilyDTO response;

    public FindFamilyByAddressIdRestResponse () {
    }

    public FamilyDTO getResponse() {
        return response;
    }

    public void setResponse(FamilyDTO response) {
        this.response = response;
    }
}
