// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingRechargeRates;
import com.everhomes.util.StringHelper;

public class ParkingRechargeRate extends EhParkingRechargeRates {
    private static final long serialVersionUID = 2762998493345176903L;

    public ParkingRechargeRate() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
