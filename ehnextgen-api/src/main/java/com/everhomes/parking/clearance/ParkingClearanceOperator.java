package com.everhomes.parking.clearance;

import com.everhomes.server.schema.tables.pojos.EhParkingClearanceOperators;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/12/3.
 */
public class ParkingClearanceOperator extends EhParkingClearanceOperators {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
