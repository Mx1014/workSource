package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMeters;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/10/26.
 */
public class EnergyMeter extends EhEnergyMeters {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
