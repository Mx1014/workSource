// @formatter:off
// generated at 2015-10-21 17:44:17
package com.everhomes.user.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.admin.UsersWithAddrResponse;

public class UserRestResponse extends RestResponseBase {

    private UsersWithAddrResponse response;

    public UserRestResponse () {
    }

    public UsersWithAddrResponse getResponse() {
        return response;
    }

    public void setResponse(UsersWithAddrResponse response) {
        this.response = response;
    }
}
