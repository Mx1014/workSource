// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.openapi;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.SynThridUserCommand;

public class UserRestResponse extends RestResponseBase {

    private SynThridUserCommand response;

    public UserRestResponse () {
    }

    public SynThridUserCommand getResponse() {
        return response;
    }

    public void setResponse(SynThridUserCommand response) {
        this.response = response;
    }
}
