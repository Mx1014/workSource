// @formatter:off
// generated at 2015-09-08 16:00:43
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
