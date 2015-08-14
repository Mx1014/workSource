// @formatter:off
// generated at 2015-08-14 09:54:22
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class QueryTopicsByCategoryRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public QueryTopicsByCategoryRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
