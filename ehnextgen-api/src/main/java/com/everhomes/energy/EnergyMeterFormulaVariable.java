package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFormulaVariables;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/10/31.
 */
public class EnergyMeterFormulaVariable extends EhEnergyMeterFormulaVariables {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
