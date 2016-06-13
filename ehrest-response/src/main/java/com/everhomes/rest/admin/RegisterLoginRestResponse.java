// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.rest.user.UserLoginDTO;

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
