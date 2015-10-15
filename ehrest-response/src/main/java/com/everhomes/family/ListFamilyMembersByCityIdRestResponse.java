// @formatter:off
// generated at 2015-10-15 10:18:58
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyMemberDTO;

public class ListFamilyMembersByCityIdRestResponse extends RestResponseBase {

    private List<FamilyMemberDTO> response;

    public ListFamilyMembersByCityIdRestResponse () {
    }

    public List<FamilyMemberDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyMemberDTO> response) {
        this.response = response;
    }
}
