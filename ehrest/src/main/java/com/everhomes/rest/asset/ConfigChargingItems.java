//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/19.
 */
/**
 *<ul>
 * <li>chargingItemId:收费项目id</li>
 * <li>projectChargingItemName:园区自定义的收费项目名字</li>
 *</ul>
 */
public class ConfigChargingItems {
    private Long chargingItemId;
    private String projectChargingItemName;

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public String getProjectChargingItemName() {
        return projectChargingItemName;
    }

    public void setProjectChargingItemName(String projectChargingItemName) {
        this.projectChargingItemName = projectChargingItemName;
    }
}
