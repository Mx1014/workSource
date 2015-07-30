// @formatter:off
// generated at 2015-07-30 19:26:52
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
