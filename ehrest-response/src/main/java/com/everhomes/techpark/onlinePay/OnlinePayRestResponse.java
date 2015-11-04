// @formatter:off
// generated at 2015-11-03 16:20:54
package com.everhomes.techpark.onlinePay;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.techpark.park.RechargeInfo;

public class OnlinePayRestResponse extends RestResponseBase {

    private RechargeInfo response;

    public OnlinePayRestResponse () {
    }

    public RechargeInfo getResponse() {
        return response;
    }

    public void setResponse(RechargeInfo response) {
        this.response = response;
    }
}
