// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyMemberDTO;

public class ListOwningFamilyMembersRestResponse extends RestResponseBase {

    private List<FamilyMemberDTO> response;

    public ListOwningFamilyMembersRestResponse () {
    }

    public List<FamilyMemberDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyMemberDTO> response) {
        this.response = response;
    }
}
