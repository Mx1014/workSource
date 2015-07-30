// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.pusher;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.device.DeviceDTO;

public class RegistDeviceRestResponse extends RestResponseBase {

    private DeviceDTO response;

    public RegistDeviceRestResponse () {
    }

    public DeviceDTO getResponse() {
        return response;
    }

    public void setResponse(DeviceDTO response) {
        this.response = response;
    }
}
