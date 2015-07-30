// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.community.admin;

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
