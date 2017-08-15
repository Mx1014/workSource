//@formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/8/11.
 */
/**
 *<ul>
 * <li>addressId:地址id</li>
 * <li>addressName:地址名称</li>
 * <li>areaSize:面积</li>
 *</ul>
 */
public class ListAddressIdsByOrganizationIdDTO {
    private Long addressId;
    private String addressName;
    private Double areaSize;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    public ListAddressIdsByOrganizationIdDTO() {

    }
}
