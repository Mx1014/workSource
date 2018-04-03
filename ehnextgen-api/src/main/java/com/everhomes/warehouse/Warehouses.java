package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouses;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public class Warehouses extends EhWarehouses {

    private static final long serialVersionUID = -1233121805542299789L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
