package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouseRequests;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public class WarehouseRequests extends EhWarehouseRequests {
    private static final long serialVersionUID = 1088757589262993393L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
