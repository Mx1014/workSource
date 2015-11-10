// @formatter:off
// generated at 2015-11-10 14:30:36
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
