// @formatter:off
// generated at 2015-07-11 14:26:36
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
