// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.community;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.address.CommunityDTO;

public class GetRestResponse extends RestResponseBase {

    private CommunityDTO response;

    public GetRestResponse () {
    }

    public CommunityDTO getResponse() {
        return response;
    }

    public void setResponse(CommunityDTO response) {
        this.response = response;
    }
}
