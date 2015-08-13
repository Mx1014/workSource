// @formatter:off
// generated at 2015-08-13 10:45:22
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserLoginDTO;

public class RegisterLoginRestResponse extends RestResponseBase {

    private UserLoginDTO response;

    public RegisterLoginRestResponse () {
    }

    public UserLoginDTO getResponse() {
        return response;
    }

    public void setResponse(UserLoginDTO response) {
        this.response = response;
    }
}
