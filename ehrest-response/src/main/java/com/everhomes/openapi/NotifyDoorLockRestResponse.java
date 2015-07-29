// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.openapi;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.openapi.NotifyDoorMessageResponse;

public class NotifyDoorLockRestResponse extends RestResponseBase {

    private NotifyDoorMessageResponse response;

    public NotifyDoorLockRestResponse () {
    }

    public NotifyDoorMessageResponse getResponse() {
        return response;
    }

    public void setResponse(NotifyDoorMessageResponse response) {
        this.response = response;
    }
}
