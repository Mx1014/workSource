// @formatter:off
// generated at 2015-05-21 22:00:49
package com.everhomes.pm;

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
