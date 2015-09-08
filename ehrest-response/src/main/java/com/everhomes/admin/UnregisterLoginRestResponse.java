// @formatter:off
// generated at 2015-09-08 16:00:43
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserLoginDTO;

public class UnregisterLoginRestResponse extends RestResponseBase {

    private UserLoginDTO response;

    public UnregisterLoginRestResponse () {
    }

    public UserLoginDTO getResponse() {
        return response;
    }

    public void setResponse(UserLoginDTO response) {
        this.response = response;
    }
}
