// @formatter:off
// generated at 2015-11-20 09:40:32
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
