// @formatter:off
// generated at 2015-08-18 15:16:38
package com.everhomes.launchpad;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.launchpad.LaunchPadLayoutDTO;

public class GetLastLaunchPadLayoutByVersionCodeRestResponse extends RestResponseBase {

    private LaunchPadLayoutDTO response;

    public GetLastLaunchPadLayoutByVersionCodeRestResponse () {
    }

    public LaunchPadLayoutDTO getResponse() {
        return response;
    }

    public void setResponse(LaunchPadLayoutDTO response) {
        this.response = response;
    }
}
