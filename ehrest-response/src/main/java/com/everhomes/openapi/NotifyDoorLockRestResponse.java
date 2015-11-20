// @formatter:off
// generated at 2015-11-20 09:40:32
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
