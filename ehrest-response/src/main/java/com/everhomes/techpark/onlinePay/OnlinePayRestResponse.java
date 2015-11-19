// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.techpark.onlinePay;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.techpark.park.RechargeInfoDTO;

public class OnlinePayRestResponse extends RestResponseBase {

    private RechargeInfoDTO response;

    public OnlinePayRestResponse () {
    }

    public RechargeInfoDTO getResponse() {
        return response;
    }

    public void setResponse(RechargeInfoDTO response) {
        this.response = response;
    }
}
