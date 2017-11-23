package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>readList: 表的读数列表，参考 {@link com.everhomes.rest.energy.ReadEnergyMeterCommand}</li>
 * </ul>
 * Created by ying.xiong on 2017/6/27.
 */
public class BatchReadEnergyMeterCommand {
    @ItemType(ReadEnergyMeterCommand.class)
    private List<ReadEnergyMeterCommand> readList;

    public List<ReadEnergyMeterCommand> getReadList() {
        return readList;
    }

    public void setReadList(List<ReadEnergyMeterCommand> readList) {
        this.readList = readList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
