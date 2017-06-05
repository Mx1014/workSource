package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouseStocks;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public class WarehouseStocks extends EhWarehouseStocks {
    private static final long serialVersionUID = -3821751547236556375L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
