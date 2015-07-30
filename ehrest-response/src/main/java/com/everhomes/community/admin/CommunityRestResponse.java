// @formatter:off
// generated at 2015-07-30 15:54:52
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
