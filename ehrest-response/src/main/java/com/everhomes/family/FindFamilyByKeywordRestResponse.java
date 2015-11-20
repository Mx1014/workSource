// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyDTO;

public class FindFamilyByKeywordRestResponse extends RestResponseBase {

    private List<FamilyDTO> response;

    public FindFamilyByKeywordRestResponse () {
    }

    public List<FamilyDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyDTO> response) {
        this.response = response;
    }
}
