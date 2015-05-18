// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.family.FamilyDTO;

public class FindFamilyByKeywordRestResponse extends RestResponseBase {

    private FamilyDTO response;

    public FindFamilyByKeywordRestResponse () {
    }

    public FamilyDTO getResponse() {
        return response;
    }

    public void setResponse(FamilyDTO response) {
        this.response = response;
    }
}
