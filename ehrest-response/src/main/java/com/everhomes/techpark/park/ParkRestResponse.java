// @formatter:off
// generated at 2015-11-03 16:20:54
package com.everhomes.techpark.park;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.techpark.park.RechargeSuccessResponse;

public class ParkRestResponse extends RestResponseBase {

    private RechargeSuccessResponse response;

    public ParkRestResponse () {
    }

    public RechargeSuccessResponse getResponse() {
        return response;
    }

    public void setResponse(RechargeSuccessResponse response) {
        this.response = response;
    }
}
