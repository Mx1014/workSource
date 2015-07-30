// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.launchpad;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.launchpad.LaunchPadItemDTO;

public class GetLaunchPadItemByIdRestResponse extends RestResponseBase {

    private LaunchPadItemDTO response;

    public GetLaunchPadItemByIdRestResponse () {
    }

    public LaunchPadItemDTO getResponse() {
        return response;
    }

    public void setResponse(LaunchPadItemDTO response) {
        this.response = response;
    }
}
