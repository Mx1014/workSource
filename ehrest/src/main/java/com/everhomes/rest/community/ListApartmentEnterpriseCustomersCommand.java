package com.everhomes.rest.community;

import javax.validation.constraints.NotNull;

/**
 * Created by ying.xiong on 2018/1/22.
 */
public class ListApartmentEnterpriseCustomersCommand {
    @NotNull
    private Long addressId;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
