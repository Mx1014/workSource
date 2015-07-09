// @formatter:off
// generated at 2015-07-09 01:51:43
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
