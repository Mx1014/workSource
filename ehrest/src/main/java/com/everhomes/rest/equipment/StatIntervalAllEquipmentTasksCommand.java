package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/4/18.
 */
public class StatIntervalAllEquipmentTasksCommand {
    private Long targetId;

    private String targetType;

    private Long inspectionCategoryId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
