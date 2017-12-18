package com.everhomes.reserve;

import com.everhomes.server.schema.tables.pojos.EhReserveOrders;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class ReserveOrder extends EhReserveOrders {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
