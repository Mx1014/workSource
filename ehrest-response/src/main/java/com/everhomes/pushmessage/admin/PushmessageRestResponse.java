// @formatter:off
// generated at 2015-10-14 12:36:35
package com.everhomes.pushmessage.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pushmessage.ListPushMessageResponse;

public class PushmessageRestResponse extends RestResponseBase {

    private ListPushMessageResponse response;

    public PushmessageRestResponse () {
    }

    public ListPushMessageResponse getResponse() {
        return response;
    }

    public void setResponse(ListPushMessageResponse response) {
        this.response = response;
    }
}
