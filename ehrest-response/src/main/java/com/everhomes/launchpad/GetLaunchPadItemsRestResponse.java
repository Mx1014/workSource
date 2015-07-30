// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.launchpad;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.launchpad.GetLaunchPadItemsCommandResponse;

public class GetLaunchPadItemsRestResponse extends RestResponseBase {

    private GetLaunchPadItemsCommandResponse response;

    public GetLaunchPadItemsRestResponse () {
    }

    public GetLaunchPadItemsCommandResponse getResponse() {
        return response;
    }

    public void setResponse(GetLaunchPadItemsCommandResponse response) {
        this.response = response;
    }
}
