// @formatter:off
// generated at 2015-10-27 13:49:25
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
