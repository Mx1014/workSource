//@formatter:off
package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouseOrders;
import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/24.
 */

public class WarehouseOrder extends EhWarehouseOrders {
    private static final long serialVersionUID = 9007681159812734395L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
