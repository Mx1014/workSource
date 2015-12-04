// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.pushmessage.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pushmessage.ListPushMessageResultResponse;

public class PushmessageRestResponse extends RestResponseBase {

    private ListPushMessageResultResponse response;

    public PushmessageRestResponse () {
    }

    public ListPushMessageResultResponse getResponse() {
        return response;
    }

    public void setResponse(ListPushMessageResultResponse response) {
        this.response = response;
    }
}
