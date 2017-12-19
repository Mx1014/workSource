package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingSpaces;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class ParkingSpace extends EhParkingSpaces {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
