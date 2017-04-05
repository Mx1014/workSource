package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMeterPriceConfig;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/3/15.
 */
public class EnergyMeterPriceConfig extends EhEnergyMeterPriceConfig {

    private static final long serialVersionUID = 7001556062463154087L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
