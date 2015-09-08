// @formatter:off
// generated at 2015-09-08 19:31:04
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.LogonCommandResponse;

public class LogonByTokenRestResponse extends RestResponseBase {

    private LogonCommandResponse response;

    public LogonByTokenRestResponse () {
    }

    public LogonCommandResponse getResponse() {
        return response;
    }

    public void setResponse(LogonCommandResponse response) {
        this.response = response;
    }
}
