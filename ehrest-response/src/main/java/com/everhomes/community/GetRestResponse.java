// @formatter:off
// generated at 2015-12-04 14:52:02
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
