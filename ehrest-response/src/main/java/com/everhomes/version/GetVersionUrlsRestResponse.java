// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.version;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.version.VersionUrlResponse;

public class GetVersionUrlsRestResponse extends RestResponseBase {

    private VersionUrlResponse response;

    public GetVersionUrlsRestResponse () {
    }

    public VersionUrlResponse getResponse() {
        return response;
    }

    public void setResponse(VersionUrlResponse response) {
        this.response = response;
    }
}
