// @formatter:off
// generated at 2015-10-27 15:08:14
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
