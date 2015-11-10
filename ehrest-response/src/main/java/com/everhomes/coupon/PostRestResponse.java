// @formatter:off
// generated at 2015-11-10 14:30:36
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
