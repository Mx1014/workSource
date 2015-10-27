// @formatter:off
// generated at 2015-10-27 15:08:14
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyDTO;

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
