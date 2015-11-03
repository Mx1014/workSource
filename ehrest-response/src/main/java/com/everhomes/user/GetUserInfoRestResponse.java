// @formatter:off
// generated at 2015-11-03 16:20:54
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
