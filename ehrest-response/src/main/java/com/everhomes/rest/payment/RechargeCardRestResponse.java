// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.payment;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.rest.order.CommonOrderDTO;

public class RechargeCardRestResponse extends RestResponseBase {

    private CommonOrderDTO response;

    public RechargeCardRestResponse () {
    }

    public CommonOrderDTO getResponse() {
        return response;
    }

    public void setResponse(CommonOrderDTO response) {
        this.response = response;
    }
}
