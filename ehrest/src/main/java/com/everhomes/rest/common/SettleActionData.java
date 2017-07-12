// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

public class SettleActionData {

    private String rentType;

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
