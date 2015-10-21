// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:18
>>>>>>> update ehrest-response 2015/10/21
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
