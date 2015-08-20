// @formatter:off
// generated at 2015-08-20 18:05:54
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
