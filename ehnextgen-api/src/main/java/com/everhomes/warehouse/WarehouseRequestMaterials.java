package com.everhomes.warehouse;

import com.everhomes.server.schema.tables.pojos.EhWarehouseRequestMaterials;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public class WarehouseRequestMaterials extends EhWarehouseRequestMaterials {
    private static final long serialVersionUID = 3757792319392563177L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
