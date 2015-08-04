// @formatter:off
// generated at 2015-08-04 16:41:43
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.UserInfo;

public class GetUserSnapshotInfoRestResponse extends RestResponseBase {

    private UserInfo response;

    public GetUserSnapshotInfoRestResponse () {
    }

    public UserInfo getResponse() {
        return response;
    }

    public void setResponse(UserInfo response) {
        this.response = response;
    }
}
