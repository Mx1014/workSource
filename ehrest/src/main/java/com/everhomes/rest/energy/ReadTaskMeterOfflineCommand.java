package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/4.
 */
public class ReadTaskMeterOfflineCommand {

    @ItemType(ReadTaskMeterCommand.class)
    private List<ReadTaskMeterCommand> meterReading;

    public List<ReadTaskMeterCommand> getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(List<ReadTaskMeterCommand> meterReading) {
        this.meterReading = meterReading;
    }
}
