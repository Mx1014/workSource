// @formatter:off
// generated at 2015-05-16 21:41:03
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
