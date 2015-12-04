// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class QueryTopicsByEntityAndCategoryRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public QueryTopicsByEntityAndCategoryRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
