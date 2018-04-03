package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingInvoiceTypes;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/18.
 */
public class ParkingInvoiceType extends EhParkingInvoiceTypes {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
