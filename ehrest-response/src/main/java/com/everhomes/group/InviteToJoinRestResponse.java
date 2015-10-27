// @formatter:off
// generated at 2015-10-27 15:08:14
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
