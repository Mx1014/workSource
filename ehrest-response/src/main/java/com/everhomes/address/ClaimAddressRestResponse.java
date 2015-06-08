// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.address.ClaimedAddressInfo;

public class ClaimAddressRestResponse extends RestResponseBase {

    private ClaimedAddressInfo response;

    public ClaimAddressRestResponse () {
    }

    public ClaimedAddressInfo getResponse() {
        return response;
    }

    public void setResponse(ClaimedAddressInfo response) {
        this.response = response;
    }
}
