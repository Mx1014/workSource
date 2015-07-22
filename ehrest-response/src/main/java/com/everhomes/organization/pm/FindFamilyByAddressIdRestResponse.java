// @formatter:off
// generated at 2015-07-22 15:04:22
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.PropFamilyDTO;

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
