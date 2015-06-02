// @formatter:off
// generated at 2015-06-01 21:15:04
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
