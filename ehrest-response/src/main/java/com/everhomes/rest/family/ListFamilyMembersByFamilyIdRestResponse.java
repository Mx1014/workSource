// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.family.FamilyMemberDTO;

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
