// @formatter:off
// generated at 2015-07-22 15:04:22
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
