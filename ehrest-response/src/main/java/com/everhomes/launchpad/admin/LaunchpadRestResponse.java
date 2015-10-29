// @formatter:off
// generated at 2015-10-29 16:34:51
package com.everhomes.launchpad.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.launchpad.LaunchPadLayoutDTO;

public class LaunchpadRestResponse extends RestResponseBase {

    private LaunchPadLayoutDTO response;

    public LaunchpadRestResponse () {
    }

    public LaunchPadLayoutDTO getResponse() {
        return response;
    }

    public void setResponse(LaunchPadLayoutDTO response) {
        this.response = response;
    }
}
