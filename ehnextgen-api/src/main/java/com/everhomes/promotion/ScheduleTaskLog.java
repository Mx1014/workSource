package com.everhomes.promotion;

import com.everhomes.server.schema.tables.pojos.EhScheduleTaskLogs;
import com.everhomes.util.StringHelper;

public class ScheduleTaskLog extends EhScheduleTaskLogs {

    /**
     * 
     */
    private static final long serialVersionUID = 379098442835186965L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}