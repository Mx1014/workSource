//@formatter:off
package com.everhomes.rest.address;

/**
 * Created by Wentian Wang on 2017/8/25.
 */

/**
 *<ul>
 * <li>buildingName:楼栋名称</li>
 * <li>addressId:地址id</li>
 *</ul>
 */
public class GetApartmentNameByBuildingNameDTO {
    private String buildingName;
    private Long addressId;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
