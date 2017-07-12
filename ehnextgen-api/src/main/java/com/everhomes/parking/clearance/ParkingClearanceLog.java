package com.everhomes.parking.clearance;

import com.everhomes.server.schema.tables.pojos.EhParkingClearanceLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/12/3.
 */
public class ParkingClearanceLog extends EhParkingClearanceLogs {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
