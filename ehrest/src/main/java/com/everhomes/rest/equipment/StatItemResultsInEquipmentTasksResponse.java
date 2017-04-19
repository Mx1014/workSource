package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by ying.xiong on 2017/4/19.
 */
public class StatItemResultsInEquipmentTasksResponse {

    private String equipmentName;

    private String customNumber;

    private String location;

    @ItemType(ItemResultStat.class)
    private List<ItemResultStat> itemResultStat;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
