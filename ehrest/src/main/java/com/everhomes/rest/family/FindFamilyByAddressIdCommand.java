// @formatter:off
package com.everhomes.rest.family;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>addressId: 关键字</li>
 * </ul>
 */
public class FindFamilyByAddressIdCommand {
    @NotNull
    private Long addressId;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public FindFamilyByAddressIdCommand () {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
