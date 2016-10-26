package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhMeters;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/10/26.
 */
public class Meter extends EhMeters {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
