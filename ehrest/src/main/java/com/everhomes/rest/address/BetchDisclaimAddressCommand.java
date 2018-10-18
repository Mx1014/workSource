package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>addressIds: 门牌地址集合</li>
 * </ul>
 */
public class BetchDisclaimAddressCommand {
    //门牌地址集合
    private List<Long> addressIds;

    public List<Long> getAddressIds() {
        return addressIds;
    }

    public void setAddressIds(List<Long> addressIds) {
        this.addressIds = addressIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
