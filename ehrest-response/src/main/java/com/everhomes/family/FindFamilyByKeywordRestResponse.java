// @formatter:off
// generated at 2015-06-07 22:21:19
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
