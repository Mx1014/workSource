// @formatter:off
// generated at 2015-07-11 14:26:36
package com.everhomes.user;

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
