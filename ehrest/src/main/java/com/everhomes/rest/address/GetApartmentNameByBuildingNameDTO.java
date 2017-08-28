//@formatter:off
package com.everhomes.rest.address;

/**
 * Created by Wentian Wang on 2017/8/25.
 */

/**
 *<ul>
 * <li>apartmentName:楼栋名称</li>
 * <li>addressId:地址id</li>
 *</ul>
 */
public class GetApartmentNameByBuildingNameDTO {
    private String apartmentName;
    private Long addressId;

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
