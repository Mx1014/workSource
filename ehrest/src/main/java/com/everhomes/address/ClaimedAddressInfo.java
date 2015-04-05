// @formatter:off
package com.everhomes.address;

import com.everhomes.util.StringHelper;

public class ClaimedAddressInfo {
    private Long addressId;
    
    private String fullAddress;
    private Integer userCount;
    
    public ClaimedAddressInfo() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
