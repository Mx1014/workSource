// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.util.StringHelper;

public class ParkingLot extends EhParkingLots {
    private static final long serialVersionUID = 4551895615872424333L;

    public ParkingLot() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
