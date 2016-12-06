package com.everhomes.activity;

import com.everhomes.server.schema.tables.pojos.EhActivityGoods;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2016/12/5.
 */
public class ActivityGoods extends EhActivityGoods {
    private static final long serialVersionUID = -8094886448634600274L;

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
