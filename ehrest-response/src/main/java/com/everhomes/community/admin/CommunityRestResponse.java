// @formatter:off
// generated at 2015-11-03 16:20:53
// generated at 2015-10-21 17:44:17
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
// generated at 2015-10-27 15:48:23
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
