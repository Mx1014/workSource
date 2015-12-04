// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.address.CommunityDTO;

public class SetCurrentCommunityRestResponse extends RestResponseBase {

    private CommunityDTO response;

    public SetCurrentCommunityRestResponse () {
    }

    public CommunityDTO getResponse() {
        return response;
    }

    public void setResponse(CommunityDTO response) {
        this.response = response;
    }
}
