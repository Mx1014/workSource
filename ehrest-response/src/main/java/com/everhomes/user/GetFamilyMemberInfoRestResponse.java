// @formatter:off
// generated at 2015-10-21 17:44:18
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
