// @formatter:off
// generated at 2015-10-14 12:36:35
package com.everhomes.user.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.admin.ListUsersWithAddrResponse;

public class UserRestResponse extends RestResponseBase {

    private ListUsersWithAddrResponse response;

    public UserRestResponse () {
    }

    public ListUsersWithAddrResponse getResponse() {
        return response;
    }

    public void setResponse(ListUsersWithAddrResponse response) {
        this.response = response;
    }
}
