// @formatter:off
// generated at 2015-11-19 19:54:45
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
