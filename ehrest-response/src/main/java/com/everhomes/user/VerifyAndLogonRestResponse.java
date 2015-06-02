// @formatter:off
// generated at 2015-06-01 21:15:04
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.LogonCommandResponse;

public class VerifyAndLogonRestResponse extends RestResponseBase {

    private LogonCommandResponse response;

    public VerifyAndLogonRestResponse () {
    }

    public LogonCommandResponse getResponse() {
        return response;
    }

    public void setResponse(LogonCommandResponse response) {
        this.response = response;
    }
}
