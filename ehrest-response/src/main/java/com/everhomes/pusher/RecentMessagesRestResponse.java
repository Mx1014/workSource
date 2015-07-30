// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.pusher;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.messaging.DeviceMessages;

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
