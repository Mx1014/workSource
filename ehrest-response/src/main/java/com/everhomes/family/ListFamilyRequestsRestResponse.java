// @formatter:off
// generated at 2015-05-27 21:29:38
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.FamilyMembershipRequestDTO;

public class ListFamilyRequestsRestResponse extends RestResponseBase {

    private List<FamilyMembershipRequestDTO> response;

    public ListFamilyRequestsRestResponse () {
    }

    public List<FamilyMembershipRequestDTO> getResponse() {
        return response;
    }

    public void setResponse(List<FamilyMembershipRequestDTO> response) {
        this.response = response;
    }
}
