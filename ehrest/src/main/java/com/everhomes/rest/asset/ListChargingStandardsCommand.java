//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>chargingItemId:收费项目id</li>
 *</ul>
 */
public class ListChargingStandardsCommand {
    @NotNull
    private Long chargingItemId;
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
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

    public ListChargingStandardsCommand() {

    }
}
