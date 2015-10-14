// @formatter:off
// generated at 2015-10-14 12:36:35
package com.everhomes.family.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.ListWaitApproveFamilyCommandResponse;

public class FamilyRestResponse extends RestResponseBase {

    private List<ListWaitApproveFamilyCommandResponse> response;

    public FamilyRestResponse () {
    }

    public List<ListWaitApproveFamilyCommandResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ListWaitApproveFamilyCommandResponse> response) {
        this.response = response;
    }
}
