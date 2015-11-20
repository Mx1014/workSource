// @formatter:off
// generated at 2015-11-20 09:40:32
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
