// @formatter:off
// generated at 2015-08-14 10:20:50
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.LogonCommandResponse;

public class VerifyAndLogonByIdentifierRestResponse extends RestResponseBase {

    private LogonCommandResponse response;

    public VerifyAndLogonByIdentifierRestResponse () {
    }

    public LogonCommandResponse getResponse() {
        return response;
    }

    public void setResponse(LogonCommandResponse response) {
        this.response = response;
    }
}
