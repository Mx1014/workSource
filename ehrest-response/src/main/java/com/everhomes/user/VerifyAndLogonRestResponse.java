// @formatter:off
// generated at 2015-05-27 21:29:38
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
