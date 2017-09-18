package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingCardTypes;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/18.
 */
public class ParkingCardRequestType extends EhParkingCardTypes {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
