// @formatter:off
// generated at 2015-09-08 21:01:07
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.CommandResult;

public class InviteToJoinRestResponse extends RestResponseBase {

    private List<CommandResult> response;

    public InviteToJoinRestResponse () {
    }

    public List<CommandResult> getResponse() {
        return response;
    }

    public void setResponse(List<CommandResult> response) {
        this.response = response;
    }
}
