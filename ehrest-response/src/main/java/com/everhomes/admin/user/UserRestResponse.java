// @formatter:off
// generated at 2015-06-07 22:21:19
package com.everhomes.admin.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.ListUserRelationResponse;

public class UserRestResponse extends RestResponseBase {

    private ListUserRelationResponse response;

    public UserRestResponse () {
    }

    public ListUserRelationResponse getResponse() {
        return response;
    }

    public void setResponse(ListUserRelationResponse response) {
        this.response = response;
    }
}
