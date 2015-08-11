// @formatter:off
// generated at 2015-08-11 15:30:30
package com.everhomes.coupon;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.coupon.CouponDTO;

public class PostRestResponse extends RestResponseBase {

    private CouponDTO response;

    public PostRestResponse () {
    }

    public CouponDTO getResponse() {
        return response;
    }

    public void setResponse(CouponDTO response) {
        this.response = response;
    }
}
