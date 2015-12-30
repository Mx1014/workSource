// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.launchpad.admin;

import com.everhomes.rest.launchpad.LaunchPadLayoutDTO;
import com.everhomes.rest.RestResponseBase;

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
