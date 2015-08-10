// @formatter:off
// generated at 2015-08-10 20:34:45
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
