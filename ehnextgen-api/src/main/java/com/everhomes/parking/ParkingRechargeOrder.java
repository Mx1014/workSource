// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingRechargeOrders;
import com.everhomes.util.StringHelper;

public class ParkingRechargeOrder extends EhParkingRechargeOrders {
    private static final long serialVersionUID = 6217464096347596870L;

    private Byte carPresenceFlag;

    public Byte getCarPresenceFlag() {
        return carPresenceFlag;
    }

    public void setCarPresenceFlag(Byte carPresenceFlag) {
        this.carPresenceFlag = carPresenceFlag;
    }

    public ParkingRechargeOrder() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
