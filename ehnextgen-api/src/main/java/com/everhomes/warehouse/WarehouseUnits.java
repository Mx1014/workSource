package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouseUnits;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public class WarehouseUnits extends EhWarehouseUnits {
    private static final long serialVersionUID = 1446324398422745100L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
