// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.forum.AssignedScopeDTO;

public class ForumRestResponse extends RestResponseBase {

    private List<AssignedScopeDTO> response;

    public ForumRestResponse () {
    }

    public List<AssignedScopeDTO> getResponse() {
        return response;
    }

    public void setResponse(List<AssignedScopeDTO> response) {
        this.response = response;
    }
}
