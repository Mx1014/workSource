package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMeterChangeLogs;
import com.everhomes.util.StringHelper;

public class EnergyMeterChangeLog extends EhEnergyMeterChangeLogs {

    private static final long serialVersionUID = -4088776728995004333L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
