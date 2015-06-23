// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.launchpad;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.launchpad.GetLaunchPadItemsCommandResponse;

public class ListLaunchPadByCommunityIdRestResponse extends RestResponseBase {

    private GetLaunchPadItemsCommandResponse response;

    public ListLaunchPadByCommunityIdRestResponse () {
    }

    public GetLaunchPadItemsCommandResponse getResponse() {
        return response;
    }

    public void setResponse(GetLaunchPadItemsCommandResponse response) {
        this.response = response;
    }
}
