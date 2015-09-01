// @formatter:off
// generated at 2015-09-01 15:16:07
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.LogonCommandResponse;

public class LogonRestResponse extends RestResponseBase {

    private LogonCommandResponse response;

    public LogonRestResponse () {
    }

    public LogonCommandResponse getResponse() {
        return response;
    }

    public void setResponse(LogonCommandResponse response) {
        this.response = response;
    }
}
