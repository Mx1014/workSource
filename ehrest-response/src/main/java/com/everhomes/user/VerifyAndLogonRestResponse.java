// @formatter:off
// generated at 2015-09-08 14:51:08
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
