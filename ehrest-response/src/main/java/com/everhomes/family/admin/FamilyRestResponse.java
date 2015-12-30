// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.family.admin;

import com.everhomes.rest.family.ListWaitApproveFamilyCommandResponse;
import com.everhomes.rest.RestResponseBase;

import java.util.List;

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
