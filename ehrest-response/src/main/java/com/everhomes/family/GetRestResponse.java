// @formatter:off
// generated at 2015-08-19 15:26:32
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.family.FamilyDTO;

public class GetRestResponse extends RestResponseBase {

    private FamilyDTO response;

    public GetRestResponse () {
    }

    public FamilyDTO getResponse() {
        return response;
    }

    public void setResponse(FamilyDTO response) {
        this.response = response;
    }
}
