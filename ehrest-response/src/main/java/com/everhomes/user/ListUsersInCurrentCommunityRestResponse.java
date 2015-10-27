// @formatter:off
// generated at 2015-11-03 16:20:54
// generated at 2015-10-21 17:44:18
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.CommunityStatusResponse;

public class ListUsersInCurrentCommunityRestResponse extends RestResponseBase {

    private CommunityStatusResponse response;

    public ListUsersInCurrentCommunityRestResponse () {
    }

    public CommunityStatusResponse getResponse() {
        return response;
    }

    public void setResponse(CommunityStatusResponse response) {
        this.response = response;
    }
}
