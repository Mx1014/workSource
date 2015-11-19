// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.community;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.community.admin.CommunityUserResponse;

public class ListCommunityUsersRestResponse extends RestResponseBase {

    private CommunityUserResponse response;

    public ListCommunityUsersRestResponse () {
    }

    public CommunityUserResponse getResponse() {
        return response;
    }

    public void setResponse(CommunityUserResponse response) {
        this.response = response;
    }
}
