// @formatter:off
// generated at 2015-11-10 11:13:10
package com.everhomes.group;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.group.CommandResult;

public class InviteToJoinByPhoneRestResponse extends RestResponseBase {

    private List<CommandResult> response;

    public InviteToJoinByPhoneRestResponse () {
    }

    public List<CommandResult> getResponse() {
        return response;
    }

    public void setResponse(List<CommandResult> response) {
        this.response = response;
    }
}
