// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.oauth2api;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserInfo;

public class GetUserInfoRestResponse extends RestResponseBase {

    private UserInfo response;

    public GetUserInfoRestResponse () {
    }

    public UserInfo getResponse() {
        return response;
    }

    public void setResponse(UserInfo response) {
        this.response = response;
    }
}
