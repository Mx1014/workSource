// @formatter:off
// generated at 2015-08-21 13:34:54
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.family.FamilyDTO;

public class GetOwningFamilyByIdRestResponse extends RestResponseBase {

    private FamilyDTO response;

    public GetOwningFamilyByIdRestResponse () {
    }

    public FamilyDTO getResponse() {
        return response;
    }

    public void setResponse(FamilyDTO response) {
        this.response = response;
    }
}
