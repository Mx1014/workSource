// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.admin.community;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.community.ListCommunitesByStatusCommandResponse;

public class CommunityRestResponse extends RestResponseBase {

    private ListCommunitesByStatusCommandResponse response;

    public CommunityRestResponse () {
    }

    public ListCommunitesByStatusCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListCommunitesByStatusCommandResponse response) {
        this.response = response;
    }
}
