//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>chargingStandardId: 收费标准id</li>
 *</ul>
 */
public class GetChargingStandardCommand {
    @NotNull
    private Long chargingStandardId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public GetChargingStandardCommand() {

    }
}
