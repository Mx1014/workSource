// @formatter:off
// generated at 2015-08-06 19:18:04
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
