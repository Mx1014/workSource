// @formatter:off
// generated at 2015-07-11 14:26:36
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.CommandResult;

public class InviteToJoinByFamilyRestResponse extends RestResponseBase {

    private List<CommandResult> response;

    public InviteToJoinByFamilyRestResponse () {
    }

    public List<CommandResult> getResponse() {
        return response;
    }

    public void setResponse(List<CommandResult> response) {
        this.response = response;
    }
}
