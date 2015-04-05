// @formatter:off
package com.everhomes.address;

import com.everhomes.util.StringHelper;

public class DisclaimAddressCommand {
    private Long addressId;
    
    public DisclaimAddressCommand() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
