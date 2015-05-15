// @formatter:off
package com.everhomes.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>addressId: 门牌号</li>
 * <li>apartmentName: 门牌号</li>
 * </ul>
 */
public class ApartmentDTO {
    private Long addressId;
    private String   apartmentName;
    
    public ApartmentDTO() {
    }
    
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public java.lang.String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(java.lang.String apartmentName) {
        this.apartmentName = apartmentName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
