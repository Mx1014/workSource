// @formatter:off
// generated at 2015-07-11 16:05:49
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
