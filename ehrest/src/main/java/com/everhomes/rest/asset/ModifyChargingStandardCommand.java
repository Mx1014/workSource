//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>chargingStandardId:收费标准id</li>
 * <li>chargingStandardName:收费标准名称</li>
 *</ul>
 */
public class ModifyChargingStandardCommand {
    private Long chargingStandardId;
    private String chargingStandardName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public ModifyChargingStandardCommand() {
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public String getChargingStandardName() {
        return chargingStandardName;
    }

    public void setChargingStandardName(String chargingStandardName) {
        this.chargingStandardName = chargingStandardName;
    }
}
