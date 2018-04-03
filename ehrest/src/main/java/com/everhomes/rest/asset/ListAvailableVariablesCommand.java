//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>chargingItemId:收费项目id</li>
 *</ul>
 */
public class ListAvailableVariablesCommand {
    private Long chargingItemId;

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

    public ListAvailableVariablesCommand() {

    }
}
