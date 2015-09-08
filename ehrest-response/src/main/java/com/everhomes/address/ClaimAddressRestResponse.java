// @formatter:off
// generated at 2015-09-08 14:51:07
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.family.FamilyDTO;

public class ClaimAddressRestResponse extends RestResponseBase {

    private FamilyDTO response;

    public ClaimAddressRestResponse () {
    }

    public FamilyDTO getResponse() {
        return response;
    }

    public void setResponse(FamilyDTO response) {
        this.response = response;
    }
}
