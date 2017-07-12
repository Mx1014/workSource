package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMeterCategories;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/10/31.
 */
public class EnergyMeterCategory extends EhEnergyMeterCategories {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
