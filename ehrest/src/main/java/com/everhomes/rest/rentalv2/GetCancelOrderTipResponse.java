package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2018/1/17.
 */
public class GetCancelOrderTipResponse {

    private String tip;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
