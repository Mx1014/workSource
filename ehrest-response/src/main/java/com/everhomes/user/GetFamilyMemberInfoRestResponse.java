// @formatter:off
// generated at 2015-08-20 19:14:55
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
