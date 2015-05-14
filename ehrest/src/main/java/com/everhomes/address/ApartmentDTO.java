// @formatter:off
package com.everhomes.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>apartmentName: 门牌号</li>
 * </ul>
 */
public class ApartmentDTO {
    private java.lang.String   apartmentName;
    
    public ApartmentDTO() {
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
