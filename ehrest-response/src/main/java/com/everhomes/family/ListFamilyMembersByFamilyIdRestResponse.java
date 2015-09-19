// @formatter:off
// generated at 2015-09-18 18:44:17
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyMemberDTO;

public class ListFamilyMembersByFamilyIdRestResponse extends RestResponseBase {

    private List<FamilyMemberDTO> response;

    public ListFamilyMembersByFamilyIdRestResponse () {
    }

    public List<FamilyMemberDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyMemberDTO> response) {
        this.response = response;
    }
}
