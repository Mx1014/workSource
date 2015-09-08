// @formatter:off
// generated at 2015-09-08 19:31:04
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
