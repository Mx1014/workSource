// @formatter:off
// generated at 2015-10-27 15:48:23
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class QueryOrgTopicsByCategoryRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public QueryOrgTopicsByCategoryRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
