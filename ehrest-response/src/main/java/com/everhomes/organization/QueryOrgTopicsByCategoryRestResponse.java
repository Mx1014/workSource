// @formatter:off
// generated at 2015-07-11 16:05:49
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.ListPropPostCommandResponse;

public class QueryOrgTopicsByCategoryRestResponse extends RestResponseBase {

    private ListPropPostCommandResponse response;

    public QueryOrgTopicsByCategoryRestResponse () {
    }

    public ListPropPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPropPostCommandResponse response) {
        this.response = response;
    }
}
