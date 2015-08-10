// @formatter:off
// generated at 2015-08-10 11:20:28
package com.everhomes.version;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.version.UpgradeInfoResponse;

public class GetUpgradeInfoRestResponse extends RestResponseBase {

    private UpgradeInfoResponse response;

    public GetUpgradeInfoRestResponse () {
    }

    public UpgradeInfoResponse getResponse() {
        return response;
    }

    public void setResponse(UpgradeInfoResponse response) {
        this.response = response;
    }
}
