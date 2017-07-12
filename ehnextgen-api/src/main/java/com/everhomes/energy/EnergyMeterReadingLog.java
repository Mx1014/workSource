package com.everhomes.energy;

import com.everhomes.server.schema.tables.pojos.EhEnergyMeterReadingLogs;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class EnergyMeterReadingLog extends EhEnergyMeterReadingLogs {

    private static final long serialVersionUID = -3841310695179022229L;

    // 在换表的时候, reading字段存储新表读数,
    // 该字段存储旧表读数, 但是不会存储的DB, 作为媒介传递
    private BigDecimal oldMeterReading;

    public BigDecimal getOldMeterReading() {
        return oldMeterReading;
    }

    public void setOldMeterReading(BigDecimal oldMeterReading) {
        this.oldMeterReading = oldMeterReading;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
