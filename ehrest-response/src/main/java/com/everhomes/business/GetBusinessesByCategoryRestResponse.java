// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.business;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.business.GetBusinessesByCategoryCommandResponse;

public class GetBusinessesByCategoryRestResponse extends RestResponseBase {

    private GetBusinessesByCategoryCommandResponse response;

    public GetBusinessesByCategoryRestResponse () {
    }

    public GetBusinessesByCategoryCommandResponse getResponse() {
        return response;
    }

    public void setResponse(GetBusinessesByCategoryCommandResponse response) {
        this.response = response;
    }
}
