//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午1:38:01
 */
public class ChargingVariables {
	
	private List<ChargingVariable> chargingVariables;

	public List<ChargingVariable> getChargingVariables() {
		return chargingVariables;
	}

	public void setChargingVariables(List<ChargingVariable> chargingVariables) {
		this.chargingVariables = chargingVariables;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
