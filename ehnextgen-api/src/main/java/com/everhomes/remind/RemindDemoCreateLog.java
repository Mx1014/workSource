package com.everhomes.remind;

import com.everhomes.server.schema.tables.pojos.EhRemindDemoCreateLogs;
import com.everhomes.util.StringHelper;

public class RemindDemoCreateLog extends EhRemindDemoCreateLogs {
    private static final long serialVersionUID = -320234945L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
