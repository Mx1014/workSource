package com.everhomes.promotion;

import com.everhomes.server.schema.tables.pojos.EhScheduleTasks;
import com.everhomes.util.StringHelper;

public class ScheduleTask extends EhScheduleTasks {
    /**
     * 
     */
    private static final long serialVersionUID = -2889907950601796539L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
