// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.user.GetSignatureCommandResponse;

public class GetBizSignatureRestResponse extends RestResponseBase {

    private GetSignatureCommandResponse response;

    public GetBizSignatureRestResponse () {
    }

    public GetSignatureCommandResponse getResponse() {
        return response;
    }

    public void setResponse(GetSignatureCommandResponse response) {
        this.response = response;
    }
}
