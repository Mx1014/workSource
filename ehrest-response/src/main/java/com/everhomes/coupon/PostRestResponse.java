// @formatter:off
// generated at 2015-11-03 16:20:53
// generated at 2015-10-21 17:44:17
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
// generated at 2015-10-27 15:48:23
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
