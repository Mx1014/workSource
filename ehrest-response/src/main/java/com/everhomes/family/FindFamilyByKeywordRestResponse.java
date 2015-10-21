// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:17
>>>>>>> update ehrest-response 2015/10/21
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
