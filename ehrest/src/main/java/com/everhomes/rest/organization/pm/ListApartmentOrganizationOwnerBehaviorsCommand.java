package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>addressId: 门牌id</li>
 * </ul>
 * Created by ying.xiong on 2017/11/28.
 */
public class ListApartmentOrganizationOwnerBehaviorsCommand {

    private Long addressId;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
