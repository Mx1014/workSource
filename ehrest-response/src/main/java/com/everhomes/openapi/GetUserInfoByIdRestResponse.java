// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.openapi;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserInfoFroBiz;

public class GetUserInfoByIdRestResponse extends RestResponseBase {

    private UserInfoFroBiz response;

    public GetUserInfoByIdRestResponse () {
    }

    public UserInfoFroBiz getResponse() {
        return response;
    }

    public void setResponse(UserInfoFroBiz response) {
        this.response = response;
    }
}
