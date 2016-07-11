// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingRechargeOrders;
import com.everhomes.util.StringHelper;

public class ParkingRechargeOrder extends EhParkingRechargeOrders {
    private static final long serialVersionUID = 6217464096347596870L;

    public ParkingRechargeOrder() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
