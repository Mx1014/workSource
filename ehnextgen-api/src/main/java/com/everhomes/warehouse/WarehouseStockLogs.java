package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouseStockLogs;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public class WarehouseStockLogs extends EhWarehouseStockLogs {
    private static final long serialVersionUID = 4899892258374314358L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
