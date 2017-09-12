//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>chargingItemId: 收费项目id</li>
 * <li>chargingItemName: 收费项目名称</li>
 *</ul>
 */
public class ListChargingItemsDTO {
    private Long chargingItemId;
    private String chargingItemName;

    public ListChargingItemsDTO() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
    }
}
