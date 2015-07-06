// @formatter:off
// generated at 2015-07-05 23:21:21
package com.everhomes.launchpad;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.launchpad.LaunchPadLayoutDTO;

public class GetLaunchPadLayoutRestResponse extends RestResponseBase {

    private LaunchPadLayoutDTO response;

    public GetLaunchPadLayoutRestResponse () {
    }

    public LaunchPadLayoutDTO getResponse() {
        return response;
    }

    public void setResponse(LaunchPadLayoutDTO response) {
        this.response = response;
    }
}
