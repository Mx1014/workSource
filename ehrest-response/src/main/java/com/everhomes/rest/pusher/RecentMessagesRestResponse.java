// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.pusher;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.rest.messaging.DeviceMessages;

public class RecentMessagesRestResponse extends RestResponseBase {

    private DeviceMessages response;

    public RecentMessagesRestResponse () {
    }

    public DeviceMessages getResponse() {
        return response;
    }

    public void setResponse(DeviceMessages response) {
        this.response = response;
    }
}
