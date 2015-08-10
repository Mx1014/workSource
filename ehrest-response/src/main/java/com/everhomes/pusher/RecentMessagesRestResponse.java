// @formatter:off
// generated at 2015-08-10 20:34:45
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
