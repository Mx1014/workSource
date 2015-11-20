// @formatter:off
// generated at 2015-11-20 09:40:32
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class ListOrgTopicCommentsRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public ListOrgTopicCommentsRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
