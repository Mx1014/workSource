// @formatter:off
// generated at 2015-08-10 11:20:28
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
