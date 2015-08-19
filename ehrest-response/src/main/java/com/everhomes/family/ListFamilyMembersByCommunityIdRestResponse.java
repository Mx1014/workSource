// @formatter:off
// generated at 2015-08-19 15:26:32
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyMemberDTO;

public class ListFamilyMembersByCommunityIdRestResponse extends RestResponseBase {

    private List<FamilyMemberDTO> response;

    public ListFamilyMembersByCommunityIdRestResponse () {
    }

    public List<FamilyMemberDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyMemberDTO> response) {
        this.response = response;
    }
}
