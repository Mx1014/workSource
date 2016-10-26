package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhMeterProperties;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/10/28.
 */
public class MeterProperty extends EhMeterProperties {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
