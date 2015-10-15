// @formatter:off
// generated at 2015-10-15 10:18:58
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
