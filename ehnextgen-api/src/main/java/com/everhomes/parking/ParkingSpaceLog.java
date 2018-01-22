package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingSpaceLogs;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class ParkingSpaceLog extends EhParkingSpaceLogs {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
