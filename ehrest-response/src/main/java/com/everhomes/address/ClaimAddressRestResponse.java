// @formatter:off
// generated at 2015-07-22 15:04:21
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
