// @formatter:off
// generated at 2015-08-14 10:20:50
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
