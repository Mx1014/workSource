// @formatter:off
// generated at 2015-10-27 13:49:25
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserInfo;

public class GetFamilyMemberInfoRestResponse extends RestResponseBase {

    private UserInfo response;

    public GetFamilyMemberInfoRestResponse () {
    }

    public UserInfo getResponse() {
        return response;
    }

    public void setResponse(UserInfo response) {
        this.response = response;
    }
}
