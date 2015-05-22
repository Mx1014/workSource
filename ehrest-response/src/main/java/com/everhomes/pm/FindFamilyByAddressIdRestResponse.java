// @formatter:off
// generated at 2015-05-21 22:00:49
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
