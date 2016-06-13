// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>addressId: 地址Id</li>
 * <li>fullAddress: 详细地址</li>
 * <li>userCount: 家庭成员数</li>
 * </ul>
 */
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
