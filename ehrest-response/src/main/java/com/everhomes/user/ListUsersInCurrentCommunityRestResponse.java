// @formatter:off
// generated at 2015-09-08 16:00:43
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
