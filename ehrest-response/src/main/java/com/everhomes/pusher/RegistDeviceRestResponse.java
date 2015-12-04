// @formatter:off
// generated at 2015-12-04 14:52:03
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
