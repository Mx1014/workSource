// @formatter:off
// generated at 2015-05-21 22:00:49
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
