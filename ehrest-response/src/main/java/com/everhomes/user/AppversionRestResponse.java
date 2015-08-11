// @formatter:off
// generated at 2015-08-11 15:30:31
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.local.GetAppVersion;

public class AppversionRestResponse extends RestResponseBase {

    private GetAppVersion response;

    public AppversionRestResponse () {
    }

    public GetAppVersion getResponse() {
        return response;
    }

    public void setResponse(GetAppVersion response) {
        this.response = response;
    }
}
