// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.enterprise;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.ListEnterpriseResponse;

public class SearchEnterpriseRestResponse extends RestResponseBase {

    private ListEnterpriseResponse response;

    public SearchEnterpriseRestResponse () {
    }

    public ListEnterpriseResponse getResponse() {
        return response;
    }

    public void setResponse(ListEnterpriseResponse response) {
        this.response = response;
    }
}
