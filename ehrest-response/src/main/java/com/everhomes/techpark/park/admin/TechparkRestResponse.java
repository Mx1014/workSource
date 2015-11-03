// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.techpark.park.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.techpark.park.WaitingDaysResponse;

public class TechparkRestResponse extends RestResponseBase {

    private WaitingDaysResponse response;

    public TechparkRestResponse () {
    }

    public WaitingDaysResponse getResponse() {
        return response;
    }

    public void setResponse(WaitingDaysResponse response) {
        this.response = response;
    }
}
