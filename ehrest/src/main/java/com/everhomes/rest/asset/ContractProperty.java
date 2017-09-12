//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/8/29.
 */

public class ContractProperty {
    private String buldingName;
    private String apartmentName;
    private String propertyName;
    private Long addressId;

    public String getBuldingName() {
        return buldingName;
    }

    public void setBuldingName(String buldingName) {
        this.buldingName = buldingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
