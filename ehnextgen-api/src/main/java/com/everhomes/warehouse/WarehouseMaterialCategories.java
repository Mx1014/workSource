package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouseMaterialCategories;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public class WarehouseMaterialCategories extends EhWarehouseMaterialCategories {
    private static final long serialVersionUID = -5793067281436160188L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
