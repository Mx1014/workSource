package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingUserInvoices;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/19.
 */
public class ParkingUserInvoice extends EhParkingUserInvoices {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
