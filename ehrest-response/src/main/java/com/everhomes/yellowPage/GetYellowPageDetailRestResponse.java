// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.yellowPage;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.yellowPage.YellowPageDTO;

public class GetYellowPageDetailRestResponse extends RestResponseBase {

    private YellowPageDTO response;

    public GetYellowPageDetailRestResponse () {
    }

    public YellowPageDTO getResponse() {
        return response;
    }

    public void setResponse(YellowPageDTO response) {
        this.response = response;
    }
}
