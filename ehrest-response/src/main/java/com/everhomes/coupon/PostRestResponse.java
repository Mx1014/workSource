// @formatter:off
// generated at 2015-10-30 14:21:35
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
