// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.family.FamilyDTO;

public class GetUserOwningFamiliesRestResponse extends RestResponseBase {

    private List<FamilyDTO> response;

    public GetUserOwningFamiliesRestResponse () {
    }

    public List<FamilyDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyDTO> response) {
        this.response = response;
    }
}
