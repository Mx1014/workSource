// @formatter:off
// generated at 2015-05-27 21:29:38
package com.everhomes.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pm.PropFamilyDTO;

public class FindFamilyByAddressIdRestResponse extends RestResponseBase {

    private PropFamilyDTO response;

    public FindFamilyByAddressIdRestResponse () {
    }

    public PropFamilyDTO getResponse() {
        return response;
    }

    public void setResponse(PropFamilyDTO response) {
        this.response = response;
    }
}
