// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.yellowPage;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.yellowPage.YellowPageDTO;

public class GetYellowPageDetailRestResponse extends RestResponseBase {

    private List<YellowPageDTO> response;

    public GetYellowPageDetailRestResponse () {
    }

    public List<YellowPageDTO> getResponse() {
        return response;
    }

    public void setResponse(List<YellowPageDTO> response) {
        this.response = response;
    }
}
