package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>addressId: 地址信息</li>
 * <li>livingStatus: 是否在户, 0: 不在, 1: 在</li>
 * <li>checkInDate: 迁入日期</li>
 * </ul>
 */
public class OrganizationOwnerAddressCommand {

    @NotNull private Long addressId;
    private Byte livingStatus;
    private Long checkInDate;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    public Long getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Long checkInDate) {
        this.checkInDate = checkInDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
