// @formatter:off
// generated at 2015-11-19 19:54:44
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.family.FamilyDTO;

public class ClaimAddressV2RestResponse extends RestResponseBase {

    private FamilyDTO response;

    public ClaimAddressV2RestResponse () {
    }

    public FamilyDTO getResponse() {
        return response;
    }

    public void setResponse(FamilyDTO response) {
        this.response = response;
    }
}
