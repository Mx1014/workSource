// @formatter:off
// generated at 2015-11-03 16:20:54
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
