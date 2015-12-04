// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.LogonCommandResponse;

public class AdminLogonRestResponse extends RestResponseBase {

    private LogonCommandResponse response;

    public AdminLogonRestResponse () {
    }

    public LogonCommandResponse getResponse() {
        return response;
    }

    public void setResponse(LogonCommandResponse response) {
        this.response = response;
    }
}
